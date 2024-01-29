package com.github.the_gigi.llm.functions;

import dev.langchain4j.agent.tool.Tool;

public class LangChainCompanyInfo {
  @Tool("Calculates the length of a string") 
  Functions.CompanyInfoResponse getCompanyInfo(String companyName) {
    var r = new CompanyInfoRequest();
    r.name = companyName;
    return Functions.getCompanyInfo(r);
  }
}
