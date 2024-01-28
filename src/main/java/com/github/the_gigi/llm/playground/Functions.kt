package com.github.the_gigi.llm.playground

import com.aallam.openai.api.chat.ToolCall
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive


internal fun ToolCall.Function.execute(): String {
    val functionToCall = availableFunctions[function.name] ?: error("Function ${function.name} not found")
    val functionArgs = function.argumentsAsJson()
    return functionToCall(functionArgs)
}


private val availableFunctions = mapOf(
        "get_company_info" to ::getCompanyInfo,
)

private fun getCompanyInfo(args: JsonObject): String {
    val companyName = args.getValue("companyName").jsonPrimitive.content
    val r = CompanyInfoRequest();
    r.name = companyName;
    val companyInfo = Functions.getCompanyInfo(r)

    // generate json for company info
    //val employees = Map<String, List<String>>.of()
    val employees: Map<String, List<String>> = companyInfo.employees.associate { employee ->
        employee.name to employee.previousCompanies
    }

    val json = Json.encodeToString(employees)

    return json;
}

internal fun getToolsData(): List<FunctionToolCallData> {
    return listOf(
            FunctionToolCallData(
                    functionName = "get_company_info",
                    description = "this function returns employees and their past companies",
                    arguments = listOf(
                            FunctionToolCallArgumentData(
                                    name = "companyName",
                                    description = "the name of the company, such as Netflix",
                                    type = "string",
                                    required = true,
                            )
                    )
            )
    )
}

