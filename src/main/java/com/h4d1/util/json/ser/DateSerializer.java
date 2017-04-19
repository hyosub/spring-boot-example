package com.h4d1.util.json.ser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSerializer extends JsonSerializer<Date> {

	private final String DATE_PATTERN = "yyyyMMddHHmmss";
	
	@Override
	public void serialize(Date value, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
		generator.writeString(dateFormat.format(value));
	}

}
