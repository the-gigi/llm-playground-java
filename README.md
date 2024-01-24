# llm-playground-java

Play with LLMs in Java

[TheoKanning/openai-java](https://github.com/TheoKanning/openai-java) is a comprehensive 
Java client for the OpenAI API. It is focused on the "real" OpenAI, but it can be easily
extended to support other LLM providers that are compatible with the OpenAI API.

This project provides a wrapper around the [OpenAiService](https://github.com/TheoKanning/openai-java/blob/main/service/src/main/java/com/theokanning/openai/service/OpenAiService.java) class that makes it easy to work
with any OpenAI-compatible LLM provider.

It was tested against [OpenAI]() and Anyscale

## Usage
See the [Main](src/main/java/com/github/the_gigi/llm_playground/Main.java) class for an example of how to use the wrapper.

It does a couple of simple interactions against [OpenAI](https://popenai.com) as well as [Anyscale](https://anyscale.io).
It then goes into an interactive chat session in the terminal (poor man's ChatGPT).

## Function calling

The [Chat](src/main/java/com/github/the_gigi/llm_playground/Chat.java) class supports the OpenAI API
for [function calling](https://platform.openai.com/docs/guides/function-calling).

It works with OpenAI, but unfortunately fails at the moment with Anyscale (the function is never called).

Anyscale supports function calling for the following models:
- mistralai/Mistral-7B-Instruct-v0.1
- mistralai/Mixtral-8x7B-Instruct-v0.1

See https://docs.endpoints.anyscale.com/guides/function-calling for more details.

I verified that function calling works on Anyscale when using their Python sample code.

## Other LLM Providers

In general, when working with other LLM providers it's important to understand the differences between them 
and the OpenAI API. The API compatability might be partial, so you'll need to check the 
documentation.

Here is a migration guide for the Anyscale API:
https://docs.anyscale.com/endpoints/model-serving/openai-migration-guide