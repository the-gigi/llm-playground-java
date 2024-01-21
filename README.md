# llm-playground-java

Play with LLMs in Java

[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java) is a comprehensive 
Java client for the OpenAI API. It is focused on the "real" OpenAI, but it can be easily
extended to support other LLM providers that are compatible with the OpenAI API.

This project provides a wrapper around the [OpenAiService](https://github.com/TheoKanning/openai-java/blob/main/service/src/main/java/com/theokanning/openai/service/OpenAiService.java) class that makes it easy to work
with any OpenAI-compatible LLM provider.

## Usage
See the [Main](src/main/java/com/github/the/gigi/llm/playground/Main.java) class for an example of how to use the wrapper.

It does a simple chat completion against the [OpenAI]() API as well as the [Anyscale](https://anyscale.io) API.

## Other LLM Providers

When working with other LLM providers it's important to understand the differences between them 
and the OpenAI API. The API compatability might be partial, so you'll need to check the 
documentation.

Here is a migration guide for the Anyscale API:
https://docs.anyscale.com/endpoints/model-serving/openai-migration-guide