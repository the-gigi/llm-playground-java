package com.github.the_gigi.llm_playground;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.function.Functional;
import java.util.List;
import kotlinx.serialization.Serializable;


class CompanyInfoRequest implements Functional {

  @JsonPropertyDescription("Name of company, for example: 'Microsoft' or 'Netflix")
  @JsonProperty(required = true)
  public String name;

  @Override
  public Object execute() {
    return Functions.getCompanyInfo(this);
  }
}


public class Functions {

  public record FunctionInfo(String name, String description, Class<? extends Functional> funcClass) {

  }

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


  public static List<FunctionInfo> getFunctionsData() {
    return List.of(
        new FunctionInfo(
            "get_company_info",
            "Get information about a company",
            CompanyInfoRequest.class)
    );
  }
}

