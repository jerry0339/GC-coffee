package com.example.gccoffee.model;

import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

public class Email {

    private final String address;

    public Email(String address) {
        Assert.notNull(address, "address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50, "address length must be between 4 and 50 characters.");
        Assert.isTrue(checkAddress(address), "Invalid email address");
        this.address = address;
    }

    public static boolean checkAddress(String address) {
        // 레게스를 이용한 이메일 검증: (https://regexr.com/) 참고
        // https://regexr.com/ -> Community Patterns -> email 검색 -> 괜춘한거 복붙
        return Pattern.matches("\\b[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}\\b", address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Email{");
        sb.append("address='").append(address).append('\'');
        sb.append("}");
        return sb.toString();
    }

    public String getAddress() {
        return address;
    }
}
