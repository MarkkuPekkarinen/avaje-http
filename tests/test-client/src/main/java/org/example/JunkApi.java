package org.example;

import io.avaje.http.api.Client;
import io.avaje.http.api.Get;
import io.avaje.http.api.Post;
import io.avaje.http.client.HttpCall;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Client
public interface JunkApi {

  @Post
  void asVoid();

  @Post
  HttpResponse<Void> asVoid2();

  @Post
  String asPlainString();

  @Post
  HttpResponse<String> asString2();

  // @Post byte[] asBytesErr();
  @Post
  HttpResponse<byte[]> asBytes2();

  // @Post InputStream asInputStreamErr();
  @Post
  HttpResponse<InputStream> asInputStream2();

  // @Post Stream<String> asLinesErr();
  @Post
  HttpResponse<Stream<String>> asLines2();

  @Post
  Repo bean();

  @Post
  List<Repo> list();

  @Post
  Stream<Repo> stream();

  // -------

  // @Post CompletableFuture<Void> cfVoidErr();
  @Post
  CompletableFuture<HttpResponse<Void>> cfVoid();

  // @Post  CompletableFuture<String> cfStringErr();
  @Post
  CompletableFuture<HttpResponse<String>> cfString();

  // @Post CompletableFuture<byte[]> cfBytesErr();
  @Post
  CompletableFuture<HttpResponse<byte[]>> cfBytes();

  // @Post CompletableFuture<InputStream> cfInputStreamErr2();
  @Post
  CompletableFuture<HttpResponse<InputStream>> cfInputStream();

  // @Post CompletableFuture<Stream<String>> cfLinesErr();
  @Post
  CompletableFuture<HttpResponse<Stream<String>>> cfLines();

  // @Post CompletableFuture<Void> cfVoidErr();
  @Post
  HttpCall<HttpResponse<Void>> callVoid();

  // @Post  CompletableFuture<String> cfStringErr();
  @Post
  HttpCall<HttpResponse<String>> callString();

  // @Post HttpCall<byte[]> callBytesErr();
  @Post
  HttpCall<HttpResponse<byte[]>> callBytes();

  // @Post HttpCall<InputStream> callInputStreamErr();
  @Post
  HttpCall<HttpResponse<InputStream>> callInputStream();

  // @Post HttpCall<Stream<String>> callLinesErr();
  @Post
  HttpCall<HttpResponse<Stream<String>>> callLines();

  @Get
  <E> HttpResponse<E> withHandGeneric(HttpResponse.BodyHandler<E> handler);

  @Get
  HttpResponse<Path> withHandPath(HttpResponse.BodyHandler<Path> handler);

  @Get
  <E> CompletableFuture<HttpResponse<E>> cfWithHandGeneric(HttpResponse.BodyHandler<E> handler);

  @Get
  CompletableFuture<HttpResponse<Path>> cfWithHandPath(HttpResponse.BodyHandler<Path> handler);

  @Get
  <E> HttpCall<HttpResponse<E>> callWithHandGeneric(HttpResponse.BodyHandler<E> handler);

  @Get
  HttpCall<HttpResponse<Path>> callWithHandPath(HttpResponse.BodyHandler<Path> handler);

}
