package org.chi.dpuk.sensing.platform.dto;

public class PayloadDTO {

	private String payload;
	private String payloadNameWithExtension;
	private String payloadContentType;
	private String startDate;
	private String endDate;
	private String dataSourceUserName;
	private String dataSourcePassword;

	public String getPayload() {
		return payload;
	}

	public String getPayloadNameWithExtension() {
		return payloadNameWithExtension;
	}

	public String getPayloadContentType() {
		return payloadContentType;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getDataSourceUserName() {
		return dataSourceUserName;
	}

	public String getDataSourcePassword() {
		return dataSourcePassword;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setPayloadNameWithExtension(String payloadNameWithExtension) {
		this.payloadNameWithExtension = payloadNameWithExtension;
	}

	public void setPayloadContentType(String payloadContentType) {
		this.payloadContentType = payloadContentType;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setDataSourceUserName(String dataSourceUserName) {
		this.dataSourceUserName = dataSourceUserName;
	}

	public void setDataSourcePassword(String dataSourcePassword) {
		this.dataSourcePassword = dataSourcePassword;
	}
}