package com.kurbatov.todoapp.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * This table contains confirmation tokens
 * which are used for completing registration
 */
@Entity
@Table(name = "confirmation_token")
public class ConfirmationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "confirmationTokenId")
    private Long confirmationTokenId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    public ConfirmationToken() {
    }

    @Override
    public Long getId() {
        return confirmationTokenId;
    }

    public Long getConfirmationTokenId() {
        return confirmationTokenId;
    }

    public void setConfirmationTokenId(Long confirmationTokenId) {
        this.confirmationTokenId = confirmationTokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
