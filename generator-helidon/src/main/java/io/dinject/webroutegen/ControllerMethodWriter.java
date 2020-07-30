package io.dinject.webroutegen;

import io.dinject.controller.MediaType;

import java.util.List;

/**
 * Write code to register Web route for a given controller method.
 */
class ControllerMethodWriter {

  private final MethodReader method;
  private final Append writer;
  private final WebMethod webMethod;
  private final ProcessingContext ctx;

  ControllerMethodWriter(MethodReader method, Append writer, ProcessingContext ctx) {
    this.method = method;
    this.writer = writer;
    this.webMethod = method.getWebMethod();
    this.ctx = ctx;
  }

  void writeRule() {
    final String fullPath = method.getFullPath();
    final String bodyType = method.getBodyType();
    if (bodyType != null) {
      writer.append("    rules.%s(\"%s\", Handler.create(%s.class, this::_%s));", webMethod.name().toLowerCase(), fullPath, bodyType, method.simpleName()).eol();
    } else if (method.isFormBody()) {
      writer.append("    rules.%s(\"%s\", Handler.create(%s.class, this::_%s));", webMethod.name().toLowerCase(), fullPath, "FormParams", method.simpleName()).eol();
    } else {
      writer.append("    rules.%s(\"%s\", this::_%s);", webMethod.name().toLowerCase(), fullPath, method.simpleName()).eol();
    }
  }

  void writeHandler() {
    writer.append("  private void _%s(ServerRequest req, ServerResponse res", method.simpleName());
    final String bodyType = method.getBodyType();
    if (bodyType != null) {
      writer.append(", %s %s", bodyType, method.getBodyName());
    } else if (method.isFormBody()) {
      writer.append(", %s %s", "FormParams", "formParams");
    }
    writer.append(") {").eol();
    if (!method.isVoid()) {
      writeContextReturn();
    }

    final PathSegments segments = method.getPathSegments();
    List<PathSegments.Segment> matrixSegments = segments.matrixSegments();
    for (PathSegments.Segment matrixSegment : matrixSegments) {
      matrixSegment.writeCreateSegment(writer, ctx.platform());
    }

    final List<MethodParam> params = method.getParams();
    for (MethodParam param : params) {
      param.writeCtxGet(writer, segments);
    }
    writer.append("    ");
    if (!method.isVoid()) {
      writer.append("res.send(");
    }

    if (method.includeValidate()) {
      for (MethodParam param : params) {
        param.writeValidate(writer);
      }
    }

    writer.append("controller.");
    writer.append(method.simpleName()).append("(");
    for (int i = 0; i < params.size(); i++) {
      if (i > 0) {
        writer.append(", ");
      }
      params.get(i).buildParamName(writer);
    }
    writer.append(")");
    if (!method.isVoid()) {
      writer.append(")");
    }
    writer.append(";").eol();
    writer.append("  }").eol().eol();

//    List<String> roles = method.roles();
//    if (!roles.isEmpty()) {
//      writer.append(", roles(");
//      for (int i = 0; i < roles.size(); i++) {
//        if (i > 0) {
//          writer.append(", ");
//        }
//        writer.append(Util.shortName(roles.get(i)));
//      }
//      writer.append(")");
//    }
//    writer.append(");").eol().eol();

//    writer.append("    res.send(\"Hello\");").eol();
//    writer.append("  }").eol();
  }

  private void writeContextReturn() {
    final String produces = method.getProduces();
    if (produces == null) {
      // let it be automatically set
    } else if (MediaType.APPLICATION_JSON.equalsIgnoreCase(produces)) {
      writer.append("    res.writerContext().contentType(io.helidon.common.http.MediaType.APPLICATION_JSON);").eol();
    } else if (MediaType.TEXT_HTML.equalsIgnoreCase(produces)) {
      writer.append("    res.writerContext().contentType(io.helidon.common.http.MediaType.TEXT_HTML);").eol();
    } else if (MediaType.TEXT_PLAIN.equalsIgnoreCase(produces)) {
      writer.append("    res.writerContext().contentType(io.helidon.common.http.MediaType.TEXT_PLAIN);").eol();
    } else {
      writer.append(    "res.writerContext().contentType(io.helidon.common.http.MediaType.parse(\"%s\"));", produces).eol();
    }
  }

  public void buildApiDocumentation() {
    method.buildApiDoc();
  }
}
