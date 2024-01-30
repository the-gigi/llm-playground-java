package com.github.the_gigi.llm.examples.functions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.function.Functional;

public class CompanyInfoRequest implements Functional {

  @JsonPropertyDescription("Name of company, for example: 'Microsoft' or 'Netflix")
  @JsonProperty(required = true)
  public String name;

  @Override
  public Object execute() {
    return Functions.getCompanyInfo(this);
  }
}
