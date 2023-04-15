package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.*;
import java.lang.reflect.Type;


public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
	  private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	    @Override
	    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
	        String formattedDate = src.format(formatter);
	        return new JsonPrimitive(formattedDate);
	    }

	    @Override
	    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
	        String dateString = json.getAsString();
	        return LocalDateTime.parse(dateString, formatter);
	    }
	}