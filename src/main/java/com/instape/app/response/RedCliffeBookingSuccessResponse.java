package com.instape.app.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 09-Jul-2024
 * @ModifyDate - 09-Jul-2024
 * @Desc -
 */
@JsonSerialize
public class RedCliffeBookingSuccessResponse {

	@JsonProperty("booking_extra_detail_id")
	private String bookingExtraDetailId;

	@JsonProperty("a_booking_id")
	private String bookingId;
	
	@JsonProperty("booking_price")
	private String bookingPrice;

	public String getBookingExtraDetailId() {
		return bookingExtraDetailId;
	}

	public void setBookingExtraDetailId(String bookingExtraDetailId) {
		this.bookingExtraDetailId = bookingExtraDetailId;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public String getBookingPrice() {
		return bookingPrice;
	}

	public void setBookingPrice(String bookingPrice) {
		this.bookingPrice = bookingPrice;
	}

	@Override
	public String toString() {
		return "RedCliffeBookingSuccessResponse [bookingExtraDetailId=" + bookingExtraDetailId + ", bookingId="
				+ bookingId + ", bookingPrice=" + bookingPrice + "]";
	}
}
