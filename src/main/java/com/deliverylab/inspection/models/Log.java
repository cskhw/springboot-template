package com.deliverylab.inspection.models;

import java.util.Date;
import java.util.Iterator;

import com.deliverylab.inspection.payload.request.kafka.log.CreateLogRequest;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
public class Log extends Base {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String userId;

	@NotBlank
	private String path;

	@NotBlank
	Date date;

	@NotBlank
	String ip;

	@NotBlank
	String url;

	String event;

	public Log(CreateLogRequest logReq) {
		super();
		this.userId = logReq.getUserId();
		this.path = logReq.getPath();
		this.url = logReq.getUrl();
		this.event = logReq.getEvent();
	}

}