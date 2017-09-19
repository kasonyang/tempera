package site.kason.tempera.parser;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.annotation.Nullable;
import kalang.AmbiguousMethodException;
import kalang.AstNotFoundException;
import kalang.FieldNotFoundException;
import kalang.MethodNotFoundException;
import kalang.ast.AssignExpr;
import kalang.ast.BlockStmt;
import kalang.ast.CastExpr;
import kalang.ast.ClassNode;
import kalang.ast.ConstExpr;
import kalang.ast.ElementExpr;
import kalang.ast.ExprNode;
import kalang.ast.ExprStmt;
import kalang.ast.IfStmt;
import kalang.ast.LocalVarNode;
import kalang.ast.LogicExpr;
import kalang.ast.LoopStmt;
import kalang.ast.MethodNode;
import kalang.ast.MultiStmtExpr;
import kalang.ast.NewArrayExpr;
import kalang.ast.NewObjectExpr;
import kalang.ast.ObjectFieldExpr;
import kalang.ast.ObjectInvokeExpr;
import kalang.ast.ParameterExpr;
import kalang.ast.ParameterNode;
import kalang.ast.ReturnStmt;
import kalang.ast.Statement;
import kalang.ast.ThisExpr;
import kalang.ast.UnaryExpr;
import kalang.ast.VarDeclStmt;
import kalang.ast.VarExpr;
import kalang.compiler.AstLoader;
import kalang.compiler.TypeNameResolver;
import kalang.core.ArrayType;
import kalang.core.ClassType;
import kalang.core.MethodDescriptor;
import kalang.core.ObjectType;
import kalang.core.ParameterDescriptor;
import kalang.core.PrimitiveType;
import kalang.core.Type;
import kalang.core.Types;
import kalang.core.VarTable;
import kalang.tool.ClassWriter;
import kalang.tool.MemoryOutputManager;
import kalang.util.AstUtil;
import kalang.util.BoxUtil;
import kalang.util.NameUtil;
import kalang.util.StringLiteralUtil;
import site.kason.tempera.engine.TemplateAstLoader;
import site.kason.tempera.engine.TemplateNotFoundException;
import site.kason.tempera.extension.Function;
import site.kason.tempera.lex.LexException;
import site.kason.tempera.lexer.BufferedTokenStream;
import site.kason.tempera.lexer.TexLexer;
import site.kason.tempera.lexer.TexToken;
import site.kason.tempera.lexer.TexTokenStream;
import site.kason.tempera.lexer.TexTokenType;
import static site.kason.tempera.lexer.TexTokenType.*;
import site.kason.tempera.lexer.TokenStream;
import site.kason.tempera.model.IterateContext;
import site.kason.tempera.type.TypeParser;
import site.kason.tempera.util.OffsetUtil;

/**
 *
 * @author Kason Yang
 */
public class TemplateParser {

  private static int layoutNameCounter = 0;

  private TemplateAstLoader astLoader;

  private BufferedTokenStream tokenStream;

  private TexToken token;
  private ClassNode classNode;

  private Stack<MethodNode> methodStack = new Stack();

  private Stack<VarTable<String, LocalVarNode>> varTableStack = new Stack();

  private final List<ClassNode> classes = new LinkedList();

  private Map<String, Type> nameToTypes = new HashMap();

  private final TemplateClassLoader classParser;

  private final TypeParser typePaser = new TypeParser();

  private final TypeNameResolver typeNameResolver;

  public TemplateParser(String templateName, String template, TemplateAstLoader astLoader, TemplateClassLoader classLoader) {
    this(templateName, new TexTokenStream(new TexLexer(template), TexTokenType.CHANNEL_DEFAULT), astLoader, classLoader);
  }

  public TemplateParser(String templateName, TokenStream ts, TemplateAstLoader templateAstLoader, TemplateClassLoader classParser) {
    this.classParser = classParser;
    this.astLoader = templateAstLoader;
    this.tokenStream = new BufferedTokenStream(ts);
    ClassNode cn = new ClassNode();
    cn.name = templateName;
    cn.modifier = Modifier.PUBLIC;
    try {
      cn.superType = Types.getClassType(Renderer.class.getName());
    } catch (AstNotFoundException ex) {
      throw Exceptions.unknownException(ex);
    }
    AstUtil.createEmptyConstructor(cn);
    this.classNode = cn;
    this.classes.add(cn);
    typeNameResolver = new TypeNameResolver();
    typeNameResolver.setAstLoader(AstLoader.BASE_AST_LOADER);
    typeNameResolver.importPackage("java.lang");
    typeNameResolver.importPackage("java.util");
  }

  private String getDataFieldName(String fieldName) {
    return "this_" + fieldName;
  }

  private void createDataField(ClassNode clazz) {
    for (Map.Entry<String, Type> e : this.nameToTypes.entrySet()) {
      clazz.createField(e.getValue(), this.getDataFieldName(e.getKey()), Modifier.PUBLIC);
    }
  }

  private Type scanType() throws SemanticException, LexException {
    TexToken typeToken = expect(IDENTITY);
    String cn = typeToken.getText();
    while (isToken(DOT)) {
      consume();
      typeToken = expect(IDENTITY);
      cn += "." + typeToken.getText();
    }
    String fullName = typeNameResolver.resolve(cn, classNode, classNode);
    if (fullName == null) {
      fullName = cn;
    }
    Type type;
    if (isToken(LT)) {
      List<Type> pms = new LinkedList();
      do {
        consume();
        pms.add(scanType());
      } while (isToken(COMMA));
      expect(GT);
      try {
        type = Types.getClassType(this.astLoader.loadAst(fullName), pms.toArray(new Type[pms.size()]));
      } catch (AstNotFoundException ex) {
        throw Exceptions.classNotFound(typeToken, fullName);
      }
    } else {
      try {
        type = Types.getClassType(this.astLoader.loadAst(fullName));
      } catch (AstNotFoundException ex) {
        throw Exceptions.classNotFound(typeToken, fullName);
      }
    }
    while (isToken(LBRACK)) {
      consume();
      expect(RBRACK);
      type = Types.getArrayType(type);
    }
    return type;
  }

  public Class<Renderer> parse() throws ParseException, IOException, TemplateNotFoundException {
    //this.classes.clear();
    //this.createFunctions();
    try {
      consume();
      while (isToken(START_TAG) && isTokenLA(1,VAR)) {
        consume(2);
        String name = expect(IDENTITY).getText();
        expect(COLON);
        this.setVarType(name, this.scanType());
        expect(END_TAG);
      }
      this.createDataField(classNode);
      MethodNode md = classNode.createMethodNode(Types.VOID_TYPE, "execute", Modifier.PUBLIC);
      this.enterNewMethod(md);
      this.createBlockStmt(md.getBody());
      this.exitMethod();
    } catch (LexException ex) {
      throw new LexerException(ex.getOffset(), ex.getMessage());
    }
    //currentBlock = body = md.getBody();
    MemoryOutputManager om = new MemoryOutputManager();
    ClassWriter writer = new ClassWriter(om);
    for (ClassNode c : this.classes) {
      writer.generate(c);
    }
    Class<Renderer> clazz = null;
    String mainTplName = this.classes.get(0).name;
    for (String c : om.getClassNames()) {
      Class<Renderer> clz = classParser.generateTemplateClass(c, om.getBytes(c));
      if (mainTplName.equals(c)) {
        clazz = clz;
      }
    }
    if (clazz == null) {
      throw Exceptions.unknownException("could not find the class object of template");
    }
    return clazz;
  }
  
  private void consume(int count) throws LexException{
    for(int i=0;i<count;i++){
      consume();
    }
  }

  private void consume() throws LexException {
    token = tokenStream.nextToken();
  }
  
  private boolean isTokenLA(int count,TexTokenType... type) throws LexException{
    return isToken(tokenStream.LA(count),type);
  }

  private boolean isToken(TexTokenType... type){
    return isToken(token,type);
  }
  
  private static boolean isToken(TexToken tk,TexTokenType... type) {
    for (TexTokenType t : type) {
      if (tk.getTokenType().equals(t)) {
        return true;
      }
    }
    return false;
  }

  public Statement[] body() throws LexException, IOException, TemplateNotFoundException {
    List<Statement> stmts = new LinkedList();
    for (;;) {
      if (isToken(TexTokenType.EOF)) {
        return stmts.toArray(new Statement[stmts.size()]);
      }
      Statement stmt = texStatement();
      if (stmt == null) {
        return stmts.toArray(new Statement[stmts.size()]);
      }
      stmts.add(stmt);
    }
  }

  private ExprNode getCallExpr(String methodNmae, ExprNode... args) {
    return getCallExpr(new ThisExpr(Types.getClassType(this.classNode)), methodNmae, args);
  }

  private ExprNode getNamedExpr(TexToken token) {
    String name = token.getText();
    VarTable<String, LocalVarNode> varTable = this.varTableStack.peek();
    if (varTable != null) {
      LocalVarNode var = varTable.get(name);
      if (var != null) {
        return new VarExpr(var);
      }
    }
    for (ParameterNode p : this.methodStack.peek().getParameters()) {
      if (name.equals(p.getName())) {
        return new ParameterExpr(p);
      }
    }
    if (this.classNode != null) {
      try {
        return ObjectFieldExpr.create(new ThisExpr(classNode), this.getDataFieldName(name), classNode);
      } catch (FieldNotFoundException ex) {
      }
    }
    throw Exceptions.varUndefinedException(token);
  }

  private ExprNode getCallExpr(ExprNode target, String methodName, ExprNode... args) {
    try {
      ObjectInvokeExpr expr = ObjectInvokeExpr.create(target, methodName, args);
      return expr;
    } catch (MethodNotFoundException | AmbiguousMethodException ex) {
      throw Exceptions.unknownException(ex);
    }
  }

  private ExprStmt getCallStmt(String methodName, ExprNode... exprs) {
    return new ExprStmt(this.getCallExpr(methodName, exprs));
  }
  
  private void expectEnclosdTag(TexTokenType type) throws LexException{
    expect(START_TAG);
    expect(type);
    expect(END_TAG);
  }

  private TexToken expect(TexTokenType type) throws LexException {
    if (!type.equals(token.getTokenType())) {
      throw Exceptions.unexpectedToken(token, type);
    }
    TexToken tk = token;
    consume();
    return tk;
  }

  private Statement text() throws LexException {
    Statement res = getCallStmt("append", new ConstExpr(token.getText()));
    consume();
    return res;
  }

  private Statement ifStmt() throws LexException, IOException, TemplateNotFoundException {
    expect(START_TAG);
    expect(IF);
    ExprNode condition = expr();
    if(condition==null){
      throw Exceptions.unexpectedToken(token);
    }
    if (!Types.BOOLEAN_TYPE.equals(condition.getType())) {
      condition = getCallExpr("toBoolean", condition);
    }
    expect(END_TAG);
    IfStmt ifStmt = new IfStmt(condition);
    this.createBlockStmt(ifStmt.getTrueBody());
    if (isToken(START_TAG) && isTokenLA(1,ELSE)) {
      consume(2);
      expect(END_TAG);
      this.createBlockStmt(ifStmt.getFalseBody());
    }
    expectEnclosdTag(END_IF);
    return ifStmt;
  }

  @Nullable
  private Type detectElementTypeForObjectType(@Nullable ObjectType type) {
    if (type == null) {
      return null;
    }
    ClassNode iterableClazz = Types.getIterableClassType().getClassNode();
    ClassNode typeClazz = type.getClassNode();
    if (iterableClazz.equals(typeClazz)) {
      Type[] argTypes = ((ClassType) type).getTypeArguments();
      if (argTypes == null || argTypes.length == 0) {
        return Types.getRootType();
      }
      return argTypes[0];
    } else {
      List<ObjectType> superTypes = new LinkedList(Arrays.asList(type.getInterfaces()));
      superTypes.add(type.getSuperType());
      for (ObjectType t : superTypes) {
        Type v = detectElementTypeForObjectType(t);
        if (v != null) {
          return v;
        }
      }
      return null;
    }
  }

  @Nullable
  private Type detectElementType(Type collectionType) {
    if (collectionType instanceof ArrayType) {
      return ((ArrayType) collectionType).getComponentType();
    } else {
      if (collectionType instanceof ObjectType) {
        Type elecType = this.detectElementTypeForObjectType((ObjectType) collectionType);
        if (elecType != null) {
          return elecType;
        }
      }
      return null;
    }
  }

  private Statement forStmt() throws LexException, IOException, TemplateNotFoundException {
    expect(START_TAG);
    expect(FOR);
    TexToken varToken = expect(IDENTITY);
    TexToken contextToken = null;
    if (isToken(COMMA)) {
      consume();
      contextToken = expect(IDENTITY);
    }
    expect(IN);
    ExprNode collectionExpr = expr();
    expect(END_TAG);
    
    BlockStmt forStmt = /*currentBlock =*/ new BlockStmt();
    LocalVarNode iteratorVar = this.declareLocalVar(Types.requireClassType(IterateContext.class.getName()), contextToken == null ? null : contextToken.getText());
    forStmt.statements.add(new VarDeclStmt(iteratorVar));
    Type elementType = this.detectElementType(collectionExpr.getType());
    if (elementType == null) {
      throw Exceptions.notIterableType(collectionExpr.getType(), OffsetUtil.getOffsetOfExprNode(collectionExpr));
    }
    LocalVarNode iterTmpVar = this.declareLocalVar(elementType, varToken.getText());
    forStmt.statements.add(new VarDeclStmt(iterTmpVar));
    forStmt.statements.add(new ExprStmt(new AssignExpr(new VarExpr(iteratorVar), this.getCallExpr("createIterateContext", collectionExpr))));

    ExprNode loopCondition = this.getCallExpr(new VarExpr(iteratorVar), "hasNext");
    LoopStmt loopStmt = new LoopStmt(loopCondition, null);
    loopStmt.getLoopBody().statements.add(
            new ExprStmt(
                    new AssignExpr(
                            new VarExpr(iterTmpVar), new CastExpr(
                                    elementType, this.getCallExpr(new VarExpr(iteratorVar), "next")
                            )
                    )
            )
    );
    this.createBlockStmt(loopStmt.getLoopBody());
    forStmt.statements.add(loopStmt);
    expectEnclosdTag(END_FOR);
    return forStmt;
  }

  private Statement placeholder() throws LexException, IOException, TemplateNotFoundException {
    expect(START_TAG);
    expect(PLACEHOLDER);
    TexToken id = expect(IDENTITY);
    expect(END_TAG);
    String methodName = this.createPlaceholderMethodName(id.getText());
    MethodNode m = this.classNode.createMethodNode(Types.VOID_TYPE, methodName, Modifier.PROTECTED);
    LocalVarNode[] accessibleVars = this.getAllAccessibleVars();
    VarExpr arguments[] = new VarExpr[accessibleVars.length];
    for (int i = 0; i < accessibleVars.length; i++) {
      m.createParameter(accessibleVars[i].getType(), accessibleVars[i].getName());
      arguments[i] = new VarExpr(accessibleVars[i]);
    }
    this.enterNewMethod(m);
    this.createBlockStmt(m.getBody());
    this.exitMethod();
    expectEnclosdTag(END_PLACEHOLDER);
    try {
      return new ExprStmt(ObjectInvokeExpr.create(new ThisExpr(this.classNode), methodName, arguments));
    } catch (MethodNotFoundException | AmbiguousMethodException ex) {
      throw Exceptions.unknownException(ex);
    }
  }

  private Statement layout() throws LexException, TemplateNotFoundException, IOException {
    expect(START_TAG);
    expect(LAYOUT);
    TexToken parentId = expect(IDENTITY);
    expect(END_TAG);
    ClassNode parentAst = this.astLoader.loadTemplateAst(parentId.getText());
    ClassNode oldClass = this.classNode;
    String clazzName = this.createClassNameForLayout(parentId.getText());
    ClassNode layoutClass = this.classNode = new ClassNode(clazzName, Modifier.PUBLIC);
    this.createDataField(layoutClass);
    this.classes.add(layoutClass);
    classNode.superType = Types.getClassType(parentAst);
    this.body();
    AstUtil.createEmptyConstructor(classNode);
    classNode = oldClass;
    expectEnclosdTag(END_LAYOUT);
    try {
      return new ExprStmt(
        ObjectInvokeExpr.create(
          new NewObjectExpr(Types.getClassType(layoutClass), new ExprNode[0]), "render", new ExprNode[]{
            ObjectFieldExpr.create(new ThisExpr(classNode), "data", classNode)
            , ObjectFieldExpr.create(new ThisExpr(classNode), "writer", classNode)
            //, ObjectFieldExpr.create(new ThisExpr(classNode), "functions", classNode)
            ,ObjectFieldExpr.create(new ThisExpr(classNode), "renderContext", classNode)
          }
        )
      );
    } catch (MethodNotFoundException | AmbiguousMethodException | FieldNotFoundException ex) {
      throw Exceptions.unknownException(ex);
    }
  }

  private Statement replace() throws LexException, IOException, TemplateNotFoundException {
    expect(START_TAG);
    expect(REPLACE);
    TexToken replaceToken = expect(IDENTITY);
    expect(END_TAG);
    String replaceId = replaceToken.getText();
    String replateMethodName = this.createPlaceholderMethodName(replaceId);
    ObjectType superType = this.classNode.superType;
    if (superType == null) {
      throw Exceptions.unknownException("super type is null");
    }
    MethodNode overrideMethod = null;
    for (MethodDescriptor sm : superType.getMethodDescriptors(classNode, true, true)) {
      if (replateMethodName.equals(sm.getName())) {
        overrideMethod = classNode.createMethodNode(sm.getReturnType(), sm.getName(), sm.getModifier());
        this.enterNewMethod(overrideMethod);
        for (ParameterDescriptor p : sm.getParameterDescriptors()) {
          overrideMethod.createParameter(p.getType(), p.getName());
        }
        break;
      }
    }
    if (overrideMethod == null) {
      throw new SemanticException(replaceToken.getOffset(), "placeholder not found:" + replaceId);
    }
    this.createBlockStmt(overrideMethod.getBody());
    expectEnclosdTag(END_REPLACE);
    this.exitMethod();
    return new BlockStmt();
  }

  @Nullable
  public Statement texStatement() throws LexException, IOException, TemplateNotFoundException {
    switch (token.getTokenType()) {
      case TEXT:
        return text();
      case START_TAG:
        return this.nonTextStatement();
      default: return null;
    }
  }
  
  @Nullable
  private Statement nonTextStatement() throws LexException, IOException{
    if(!isToken(START_TAG)){
      throw Exceptions.unexpectedToken(token);
    }
    TexToken la1 = tokenStream.LA(1);
    switch (la1.getTokenType()) {
      case IF:
        return ifStmt();
      case FOR:
        return forStmt();
      case PLACEHOLDER:
        return placeholder();
      case LAYOUT:
        return layout();
      case REPLACE:
        return replace();
      default:
        if(isExprPrefix(la1.getTokenType())){
          ExprStmt res;
          expect(START_TAG);
          ExprNode expr = this.expr();
          if(isToken(PIPE)){
            while(isToken(PIPE)){
              consume();
              String filterName = expect(IDENTITY).getText();
              expr = this.getCallExpr("callFilter", new ConstExpr(filterName),expr);
            }
            res = getCallStmt("rawAppend",expr);
          }else{
            res = getCallStmt("append", expr);
          }
          expect(END_TAG);
          return res;
        }else{
          return null;
        }
    }
  }
  
  private boolean isExprPrefix(TexTokenType type){
    TexTokenType[] prefixArray = new TexTokenType[]{
      LPAREN,IDENTITY,NUMBER,STRING,LOGIC_NOT,LBRACK,LBRACE
    };
    for(TexTokenType t:prefixArray){
      if(t.equals(type)) return true;
    }
    return false;
  }

  private ExprNode expr() throws LexException {
    return this.expr_logic_and_or();
  }

  public void setVarType(String key, Type type) {
    this.nameToTypes.put(key, type);
  }

  public void setVarType(String key, String type) throws ParseException, LexException {
    try {
      this.nameToTypes.put(key, this.typePaser.parse(type));
    } catch (AstNotFoundException ex) {
      throw Exceptions.classNotFound(type);
    }
  }

  private ExprNode expr_logic_and_or() throws LexException {
    ExprNode expr1 = this.expr_equals();
    if (isToken(LOGIC_AND)) {
      consume();
      ExprNode expr2 = this.expr_equals();
      return new LogicExpr(expr1, expr2, LogicExpr.OP_LOGIC_AND);
    } else if (isToken(LOGIC_OR)) {
      consume();
      ExprNode expr2 = this.expr_equals();
      return new LogicExpr(expr1, expr2, LogicExpr.OP_LOGIC_OR);
    }
    return expr1;
  }

  private ExprNode expr_equals() throws LexException {
    ExprNode expr = this.expr_add_sub();
    if (isToken(EQ)) {
      consume();
      return this.getCallExpr("eq", expr, this.expr_mul_div_mod());
    }else if(isToken(LT)){
      consume();
      return this.getCallExpr("lt", expr,this.expr_mul_div_mod());
    }else if(isToken(LE)){
      consume();
      return this.getCallExpr("le", expr,this.expr_mul_div_mod());
    }else if(isToken(GT)){
      consume();
      return this.getCallExpr("gt", expr,this.expr_mul_div_mod());
    }else if(isToken(GE)){
      consume();
      return this.getCallExpr("ge", expr,this.expr_mul_div_mod());
    }else if(isToken(NE)){
      consume();
      return this.getCallExpr("ne", expr,this.expr_mul_div_mod());
    }
    return expr;
  }

  private ExprNode expr_add_sub() throws LexException {
    ExprNode factor = this.expr_mul_div_mod();
    if (isToken(ADD)) {
      consume();
      ExprNode factor2 = this.expr_mul_div_mod();
      return this.getCallExpr("add", factor, factor2);
    } else if (isToken(SUB)) {
      consume();
      ExprNode factor2 = this.expr_mul_div_mod();
      return this.getCallExpr("sub", factor, factor2);
    } else {
      return factor;
    }
  }

  private ExprNode expr_mul_div_mod() throws LexException {
    ExprNode atom = this.atom();
    if (isToken(MUL)) {
      consume();
      ExprNode atom2 = this.atom();
      return this.getCallExpr("mul", atom, atom2);
    } else if (isToken(DIV)) {
      consume();
      ExprNode atom2 = this.atom();
      return this.getCallExpr("div", atom, atom2);
    } else if (isToken(MOD)) {
      consume();
      ExprNode atom2 = this.atom();
      return this.getCallExpr("mod", atom, atom2);
    } else {
      return atom;
    }
  }

  @Nullable
  private ExprNode detectPropertyExpr(ExprNode expr, String property) {
    try {
      return ObjectFieldExpr.create(expr, property, this.classNode);
    } catch (FieldNotFoundException ex) {
      try {
        return ObjectInvokeExpr.create(expr, property, new ExprNode[0]);
      } catch (MethodNotFoundException | AmbiguousMethodException ex1) {
        try {
          return ObjectInvokeExpr.create(expr, "get" + NameUtil.firstCharToUpperCase(property), new ExprNode[0]);
        } catch (MethodNotFoundException | AmbiguousMethodException ex2) {
          try {
            return ObjectInvokeExpr.create(expr, "is" + NameUtil.firstCharToUpperCase(property), new ExprNode[0]);
          } catch (MethodNotFoundException | AmbiguousMethodException ex3) {
            return null;
          }
        }
      }
    }
  }

  private ExprNode atom() throws LexException {
    ExprNode expr;
    if (isToken(LPAREN)) {
      consume();
      expr = this.expr();
      this.expect(RPAREN);
    } else if (isToken(IDENTITY)) {
      if(this.isTokenLA(1, LPAREN)){
        String funcName = token.getText();
        consume(2);
        List<ExprNode> argsList = new LinkedList();
        if(this.isExprPrefix(token.getTokenType())){
          argsList.add(this.expr());
        }
        while(isToken(COMMA)){
          consume();
          argsList.add(this.expr());
        }
        expect(RPAREN);
        List<Statement> initStatements = new LinkedList();
        LocalVarNode argVar = new LocalVarNode(Types.getArrayType(Types.getRootType()), null);
        initStatements.add(new VarDeclStmt(argVar));
        initStatements.add(new ExprStmt(new AssignExpr(new VarExpr(argVar),new NewArrayExpr(Types.getRootType(), new ConstExpr(argsList.size())))));
        for(int i=0;i<argsList.size();i++){
          initStatements.add(new ExprStmt(new AssignExpr(new ElementExpr(new VarExpr(argVar), new ConstExpr(i)),argsList.get(i))));
        }
        return this.getCallExpr("callFunction",new ConstExpr(funcName),new MultiStmtExpr(initStatements, new VarExpr(argVar)));
        //return this.getCallExpr("fn_"+funcName,argsList.toArray(new ExprNode[argsList.size()]));
      }else{
        expr = this.getNamedExpr(token);
        consume();
      }
    } else if (isToken(NUMBER)) {
      String text = token.getText();
      if(text.contains(".")){
        expr = new ConstExpr(Double.parseDouble(text));
      }else{
        expr = new ConstExpr(StringLiteralUtil.parseLong(text));
      }
      consume();
    } else if (isToken(STRING)) {
      String tokenText = token.getText();
      expr = new ConstExpr(
              TexLexer.LITERAL_PARSER.parse(tokenText.substring(1, tokenText.length() - 1))
      );
      consume();
    } else if (isToken(LOGIC_NOT)) {
      consume();
      ExprNode val = this.atom();
      expr = new UnaryExpr(val, "!");
    } else if (isToken(LBRACK)) {//[
      consume();
      List<Statement> statements = new LinkedList();
      List<ExprNode> elements = new LinkedList();
      elements.add(expr());
      while (isToken(COMMA)) {
        consume();
        elements.add(expr());
      }
      expect(RBRACK);
      //TODO create temp name
      LocalVarNode arrVar = new LocalVarNode(Types.getArrayType(Types.getRootType()), null);
      statements.add(new VarDeclStmt(arrVar));
      NewArrayExpr newArrExpr = new NewArrayExpr(Types.getRootType(), new ConstExpr(elements.size()));
      statements.add(new ExprStmt(new AssignExpr(new VarExpr(arrVar), newArrExpr)));
      for (int i = 0; i < elements.size(); i++) {
        statements.add(new ExprStmt(new AssignExpr(new ElementExpr(new VarExpr(arrVar), new ConstExpr(i)), elements.get(i))));
      }
      expr = new MultiStmtExpr(statements, new VarExpr(arrVar));
    } else if (isToken(LBRACE)) {
      //TODO creat map
      throw Exceptions.unexpectedToken(token);
    } else {
      throw Exceptions.unexpectedToken(this.token);
    }
    while (isToken(DOT)) {
      consume();
      TexToken propertyToken = this.expect(IDENTITY);
      String property = propertyToken.getText();
      expr = this.detectPropertyExpr(expr, property);
      if (expr == null) {
        throw Exceptions.propertyNotFound(propertyToken);
      }
    }
    Type exprType = expr.getType();
    if (exprType instanceof PrimitiveType) {
      ObjectType exprClassType = Types.getClassType((PrimitiveType)exprType);
      if(exprClassType==null){
        throw new RuntimeException("could not find class type for primitive type:" + exprType);
      }
      expr = BoxUtil.assign(expr, exprType, exprClassType);
    }
    return expr;
  }

  private void enterNewFrame() {
    VarTable<String, LocalVarNode> varTable = this.varTableStack.peek();
    this.varTableStack.add(varTable.newStack());
  }

  private void exitFrame() {
    this.varTableStack.pop();
  }

  private void createBlockStmt(BlockStmt blockStmt) throws LexException, IOException, TemplateNotFoundException {
    this.enterNewFrame();
    Statement[] stmts = this.body();
    this.exitFrame();
    blockStmt.statements.addAll(Arrays.asList(stmts));
  }

  private LocalVarNode declareLocalVar(Type type, @Nullable String name) {
    LocalVarNode var = new LocalVarNode(type, name);
    if (name != null) {
      this.varTableStack.peek().put(name, var);
    }
    return var;
  }

  private LocalVarNode[] getAllAccessibleVars() {
    List<LocalVarNode> vars = new LinkedList();
    VarTable<String, LocalVarNode> vtb = this.varTableStack.peek();
    while (vtb != null) {
      for (LocalVarNode v : vtb.values()) {
        vars.add(v);
      }
      vtb = vtb.getParent();
    }
    return vars.toArray(new LocalVarNode[vars.size()]);
  }

  private String createClassNameForLayout(String layoutName) {
    return this.classNode.name + "_" + layoutName + this.layoutNameCounter++;
  }

  private String createPlaceholderMethodName(String placeholderName) {
    return "placeholder_" + placeholderName;
  }

  public ClassNode getClassNode() {
    return classNode;
  }

  private void enterNewMethod(MethodNode method) {
    this.methodStack.add(method);
    this.varTableStack.add(new VarTable());
  }

  private void exitMethod() {
    this.methodStack.pop();
    this.varTableStack.pop();
  }
  
//  private void createFunctions(){
//    for(Function f:this.functions){
//      String returnTypeName = f.getReturnType().getName();
//      Type returnType = Types.requireClassType(returnTypeName);
//      MethodNode fnMethod = this.classNode.createMethodNode(
//              returnType,
//              "fn_" + f.getName(),
//              Modifier.PUBLIC
//      );
//      Class<?>[] params = f.getParameters();
//      for(int i=0;i<params.length;i++){
//        fnMethod.createParameter(Types.requireClassType(params[i].getName()), "arg"+i);
//      }
//      BlockStmt body = fnMethod.getBody();
//      LocalVarNode var = new LocalVarNode(Types.getArrayType(Types.getRootType()), null);
//      body.statements.add(new VarDeclStmt(var));
//      NewArrayExpr newArrayExpr = new NewArrayExpr(Types.getRootType(), new ConstExpr(params.length));
//      body.statements.add(new ExprStmt(new AssignExpr(new VarExpr(var), newArrayExpr)));
//      ParameterNode[] mdParams = fnMethod.getParameters();
//      for(int i=0;i<mdParams.length;i++){
//        body.statements.add(new ExprStmt(
//          new AssignExpr(new ElementExpr(new VarExpr(var), new ConstExpr(i)), new ParameterExpr(mdParams[i]))));
//      }
//      try{
//      body.statements.add(new ReturnStmt(
//        new CastExpr(
//          returnType
//          ,ObjectInvokeExpr.create(
//            new ThisExpr(classNode)
//            , "callFunction"
//            , new ExprNode[]{new ConstExpr(f.getName()),new VarExpr(var)}
//          )
//        )
//      ));
//      } catch (MethodNotFoundException | AmbiguousMethodException ex) {
//        throw Exceptions.unknownException(ex);
//      }
//    }
//  }

}
