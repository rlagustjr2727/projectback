package com.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "wboard")
public class Wboard {

	@Id
	@Column(name = "wboard_name", nullable = false)
	private String wboardName;

	@Column(name = "wboard_img", length = 255)
	private String wboardImg;

	@Column(name = "wboard_info", length = 1000)
	private String wboardInfo;

	@Column(name = "wboard_tip", length = 1000)
	private String wboardTip;

	@Column(name = "wboard_type", length = 20)
	private String wboardType;

	@Column(name = "wboard_origin", length = 20)
	private String wboardOrigin;

	@Column(name = "wboard_alcohol", length = 20)
	private String wboardAlcohol;

	@Column(name = "wboard_age", length = 20)
	private String wboardAge;

	@Column(name = "wboard_alcohol_type", length = 10)
	private String wboardAlcoholType;
}
