package com.github.the_gigi.llm_playground

import com.aallam.openai.api.chat.*
import com.aallam.openai.api.core.Parameters
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.runBlocking
import com.aallam.openai.api.model.Model
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


// Doesn't work on Anyscale due to different model schema (missing `created` field
fun getModels(client: OpenAI): List<Model> {
    return runBlocking {
        client.models()
    }
}


internal fun getCompletionResponse(client: OpenAI, messages: List<String>, modelId: String, tools: List<FunctionToolCallData>) : ChatMessage {

    val chatMessages : MutableList<ChatMessage> = mutableListOf();
    if (messages.isEmpty()) {
        chatMessages.add(ChatMessage(ChatRole.System, "You are a helpful assistan"))
    }

    for (m in messages) {
        val parts = m.split(",", limit = 2)
        val text = if (parts.size > 1) parts[1] else ""

        val role = ChatRole.User
        when (parts[0].trim()) {
            "assistant" -> ChatRole.Assistant
            "function" -> ChatRole.Function
            "system" -> ChatRole.System
            "tool" -> ChatRole.Tool
            else -> ChatRole.User
        }
        chatMessages.add(ChatMessage(role, text))
    }

    val chatCompletionRequest = getChatCompletionRequest(modelId, chatMessages, tools)

    return runBlocking {
        var message : ChatMessage
        while (true) {
            try {
                val completion: ChatCompletion = client.chatCompletion(chatCompletionRequest)
                message = completion.choices[0].message
                if (message.toolCalls == null  || message.toolCalls!!.isEmpty()) {
                    break
                }
                // call function
                for (toolCall in message.toolCalls.orEmpty()) {
                    require(toolCall is ToolCall.Function) { "Tool call is not a function" }
                    val result = toolCall.execute()
                    message = ChatMessage(ChatRole.System, result)
                    chatMessages.add(message)
                }

            } catch (e: Exception) {
                message = ChatMessage(ChatRole.System, "Error: ${e.message}")
                break
            }
        }
        message
    }
}


fun createOpenAiKotlinClient(baseUrl: String, token: String): OpenAI {
    val host = OpenAIHost(
            baseUrl = baseUrl,
    )
    val config = OpenAIConfig(
            host = host,
            token = token,
    )
    return OpenAI(config)
}

@Serializable
internal data class FunctionToolCallArgumentData(val name: String, val description: String, val type: String, val required: Boolean = false)

@Serializable
internal data class FunctionToolCallData(val functionName: String, val description: String, val arguments: List<FunctionToolCallArgumentData>)

@Serializable
internal data class ParametersData(val type: String, val properties: Map<String, Map<String, String>>, val required: List<String>)

private fun getParametersData(arguments: List<FunctionToolCallArgumentData>): ParametersData {
    val properties = mutableMapOf<String, Map<String, String>>()
    val required = mutableListOf<String>()
    val pd = ParametersData("object", properties, required)
    for (arg in arguments) {
        if (arg.required) {
            required.add(arg.name)
        }
        properties[arg.name] = mapOf(
                "type" to arg.type,
                "description" to arg.description)
    }
    return pd
}

internal fun getChatCompletionRequest(model: String, messages: List<ChatMessage>, toolsData: List<FunctionToolCallData>) : ChatCompletionRequest {
    val tools = ArrayList<Tool>()
    for (td in toolsData) {
        val paramData = getParametersData(td.arguments)
        val jsonString = Json.encodeToString(paramData)
        val params = Parameters.fromJsonString(jsonString)
        val func = FunctionTool(td.functionName, params)
        val tool = Tool(ToolType.Function, td.description, func)
        tools.add(tool)
    }

    val builder  = ChatCompletionRequestBuilder()
    builder.model = ModelId(model)
    builder.messages = messages
    builder.tools = tools
    builder.toolChoice = ToolChoice.Auto

    return builder.build()
}