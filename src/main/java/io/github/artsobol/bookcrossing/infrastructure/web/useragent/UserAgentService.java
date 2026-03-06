package io.github.artsobol.bookcrossing.infrastructure.web.useragent;

import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAgentService {

    private final UserAgentAnalyzer analyzer;

    public DeviceInfo parse(String userAgentString) {

        UserAgent agent = analyzer.parse(userAgentString);

        String browser = agent.getValue("AgentName");
        String browserVersion = agent.getValue("AgentVersion");
        String device = agent.getValue("DeviceName");

        return new DeviceInfo(browser, browserVersion, device);
    }
}