package com.helpmeCookies.review.entity;

import com.helpmeCookies.product.entity.Product;
import com.helpmeCookies.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

@Entity
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String content;

	@ManyToOne
	@JoinColumn(name = "writer_id", nullable = false)
	private User writer;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Builder
	public Review(Long id, String content, User writer, Product product) {
		this.id = id;
		this.content = content;
		this.writer = writer;
		this.product = product;
	}

	public Review() {}

	public void updateContent(String content) {
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public User getWriter() {
		return writer;
	}

	public Product getProduct() {
		return product;
	}
}
