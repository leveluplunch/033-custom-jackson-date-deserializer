package com.levelup;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Unit test for simple App.
 */
public class AppTest {

	class CustomDateDeseralizer extends UntypedObjectDeserializer {

		private static final long serialVersionUID = -2275951539867772400L;

		@Override
		public Object deserialize(JsonParser jp, DeserializationContext ctxt)
				throws IOException {

			if (jp.getCurrentTokenId() == JsonTokenId.ID_STRING) {
				try {
					return DateUtils.parseDate(jp.getText(), new String[] {
							"MM/dd/yyyy", "yyyy.MM.dd G 'at' HH:mm:ss z" });
				} catch (Exception e) {
					return super.deserialize(jp, ctxt);
				}
			} else {
				return super.deserialize(jp, ctxt);
			}
		}
	}

	private ObjectMapper configureObjectMapper() {

		ObjectMapper objectMapper = new ObjectMapper();

		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(Object.class, new CustomDateDeseralizer());
		objectMapper.registerModule(simpleModule);

		return objectMapper;
	}

	String objectAsJson = "{\"name\": \"Justin Musgrove\", \"shortDateFormat\": \"03/05/2015\",\"longDateFormat\": \"2001.07.04 AD at 12:08:56 PDT\"}";

	// {
	// "name": "Justin Musgrove",
	// "shortDateFormat": "03/05/2015",
	// "longDateFormat": "2001.07.04 AD at 12:08:56 PDT"
	// }
	@Test
	public void deserializeDates() throws JsonParseException,
			JsonMappingException, IOException {

		ObjectMapper objectMapper = configureObjectMapper();

		@SuppressWarnings("unchecked")
		Map<Object, Object> jsonAsMap = objectMapper.readValue(objectAsJson,
				HashMap.class);

		for (Entry<Object, Object> entry : jsonAsMap.entrySet()) {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
			System.out.println(entry.getValue().getClass());
			System.out.println("----");
		}
	}

}
