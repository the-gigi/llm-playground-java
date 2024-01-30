package com.github.the_gigi.llm.examples.functions;

import com.github.the_gigi.llm.client.SimpleOpenAiClient.FunctionInfo;
import com.theokanning.openai.completion.chat.ChatFunction;
import java.util.List;
import java.util.stream.Stream;
import kotlinx.serialization.Serializable;


public class Functions {


  public record EmployeeInfo(String name, List<String> previousCompanies) {

  }

  @Serializable
  public record CompanyInfoResponse(String name, List<EmployeeInfo> employees) {

  }


  public static CompanyInfoResponse getCompanyInfo(CompanyInfoRequest request) {
    var employees = List.of(
        new EmployeeInfo("John", List.of("SpaceX", "PayPal")),
        new EmployeeInfo("Jack", List.of("Microsoft", "SpaceX", "Netflix")),
        new EmployeeInfo("Jill", List.of("Netflix", "Amazon")),
        new EmployeeInfo("Jane", List.of("Google"))
    );
    return new CompanyInfoResponse(request.name, employees);
  }


  public static List<Object> getSimpleOpenAiTools() {
    return List.of(
        new FunctionInfo(
            "get_company_info",
            "Get information about a company",
            CompanyInfoRequest.class));
  }

  public static List<Object> getOpenAiJavaTools() {

    var function = ChatFunction.builder()
        .name("get_company_info")
        .description("Get work history of all employees of a company")
        .executor(CompanyInfoRequest.class, Functions::getCompanyInfo).build();

    return List.of(function);
  }

  public static List<Object> getLangChainTools() {
    return List.of(new LangChainCompanyInfo());
  }
}


