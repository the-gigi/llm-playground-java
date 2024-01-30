package com.github.the_gigi.llm.examples.functions;

import dev.langchain4j.agent.tool.Tool;

public class LangChainCompanyInfo {
  @Tool("Get info about a company")
  Functions.CompanyInfoResponse getCompanyInfo(String companyName) {
    var r = new CompanyInfoRequest();
    r.name = companyName;
    return Functions.getCompanyInfo(r);
  }
}
