package org.chi.dpuk.sensing.platform.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.chi.dpuk.sensing.platform.view.View;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

@Table(name = "dataSet", indexes = { @Index(name = "postDateIndex", columnList = "postDate"),
		@Index(name = "startDateIndex", columnList = "startDate"),
		@Index(name = "endDateIndex", columnList = "endDate") })

@Entity
public class DataSet {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@JsonView(View.Public.class)
	private Long id;

	@JsonSerialize(using = DateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonView(View.Public.class)
	@Column(name = "postDate", columnDefinition = "DATETIME(6)")
	private Date postDate;

	@JsonSerialize(using = DateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonView(View.Public.class)
	@Column(name = "startDate", columnDefinition = "DATETIME(6)")
	@NotNull
	private Date startDate;

	@JsonSerialize(using = DateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	@JsonView(View.Public.class)
	@Column(name = "endDate", columnDefinition = "DATETIME(6)")
	@NotNull
	private Date endDate;

	@Basic(fetch = FetchType.LAZY)
	@Column(name = "payload")
	@Lob
	@JsonView(View.Public.class)
	@NotNull
	private byte[] payload;

	@JsonView(View.Public.class)
	@NotNull
	private String payloadNameWithExtension;

	@JsonView(View.Public.class)
	@NotNull
	private String payloadContentType;

	private String dataSetHash;

	@JsonView(View.DataSet.class)
	@ManyToOne(fetch = FetchType.EAGER)
	private DataSource dataSource;

	@Transient
	private String dataSourceUserName;

	@Transient
	private String dataSourcePassword;

	public Long getId() {
		return id;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDataSetHash() {
		return dataSetHash;
	}

	public void setDataSetHash(String dataSetHash) {
		this.dataSetHash = dataSetHash;
	}

	public int getPayloadSize() {
		return payload != null ? this.payload.length : 0;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getDataSourceUserName() {
		return dataSourceUserName;
	}

	public void setDataSourceUserName(String dataSourceUserName) {
		this.dataSourceUserName = dataSourceUserName;
	}

	public String getDataSourcePassword() {
		return dataSourcePassword;
	}

	public void setDataSourcePassword(String dataSourcePassword) {
		this.dataSourcePassword = dataSourcePassword;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	public String getPayloadNameWithExtension() {
		return payloadNameWithExtension;
	}

	public void setPayloadNameWithExtension(String payloadNameWithExtension) {
		this.payloadNameWithExtension = payloadNameWithExtension;
	}

	public String getPayloadContentType() {
		return payloadContentType;
	}

	public void setPayloadContentType(String payloadContentType) {
		this.payloadContentType = payloadContentType;
	}

	@Override
	public String toString() {
		return "DataSet [id=" + id + ", postDate=" + postDate + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", payload=" + payload + ", dataSetHash=" + dataSetHash + ", payloadNameWithExtension="
				+ payloadNameWithExtension + ", payloadContentType=" + payloadContentType + "]";
	}
}