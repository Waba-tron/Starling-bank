package banking_app.Bankingapp.dto;

import java.util.List;

public class BadRequestErrorResponse {

    private String message;
    private List<FieldError> details;

    public BadRequestErrorResponse(String message, List<FieldError> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getDetails() {
        return details;
    }

    public void setDetails(List<FieldError> details) {
        this.details = details;
    }

    public static class FieldError {
        private String field;
        private String message;
        private String type;

        public FieldError(String field, String message, String type) {
            this.field = field;
            this.message = message;
            this.type = type;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
