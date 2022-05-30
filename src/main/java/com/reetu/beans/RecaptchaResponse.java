package com.reetu.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component

public class RecaptchaResponse {
            private boolean success;
            private String challenge_ts;
            private String hostName;
			public boolean isSuccess() {
				return success;
			}
			public void setSuccess(boolean success) {
				this.success = success;
			}
			public String getChallenge_ts() {
				return challenge_ts;
			}
			public void setChallenge_ts(String challenge_ts) {
				this.challenge_ts = challenge_ts;
			}
			public String getHostName() {
				return hostName;
			}
			public void setHostName(String hostName) {
				this.hostName = hostName;
			}
            
}
