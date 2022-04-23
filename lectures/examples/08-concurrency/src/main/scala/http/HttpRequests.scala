package http

import util.HttpServiceUrls

@main def runHttpRequestsExample =
  import concurrent.Executors.given

  val myIp = HttpClient.get("http://icanhazip.com")
    .map(_.getResponseBody)

  val example = HttpClient.get("http://example.org")
    .map(_.getResponseBody)

  val endResult = for
    (ipResult, exampleResult) <- (myIp zip example)
    r <- HttpClient.get(
      HttpServiceUrls.randomNumberUpTo(ipResult.length + exampleResult.length))
  yield r.getResponseBody

  endResult.foreach(println)