package org.sparta.newsfeed.user.entity;

public enum UserStatusEnum {
    ACTIVE(Status.ACTIVE),
    REMOVE(Status.REMOVE);

    private final String status;

    UserStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

    public static class Status {
        public static final String ACTIVE = "ACTIVE";
        public static final String REMOVE = "REMOVE";
    }
}
