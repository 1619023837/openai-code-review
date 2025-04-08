package hao.wen.tao.sdk.domain;

import java.util.List;


public class ChatCompletionSyncResponse
{
    private List<Choice> choices;


    public static class Choice {
        private List<Delta> delta;

        public List<Delta> getDelta() {
            return delta;
        }

        public void setDelta(List<Delta> delta) {
            this.delta = delta;
        }

        @Override
        public String toString()
        {
            return "Choice{" + "delta=" + delta + '}';
        }
    }

    public static class Delta {
        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString()
        {
            return "Message{" + "role='" + role + '\'' + ", content='" + content + '\'' + '}';
        }
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    @Override
    public String toString()
    {
        return "ChatCompletionSyncResponse{" + "choices=" + choices + '}';
    }
}
