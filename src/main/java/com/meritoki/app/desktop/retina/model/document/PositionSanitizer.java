package com.meritoki.app.desktop.retina.model.document;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.util.StdConverter;

public class PositionSanitizer extends StdConverter<Position, Position> {
	private static Logger logger = LogManager.getLogger(Position.class.getName());
	@Override
	public Position convert(Position position) {
		logger.info("convert("+position+")");
		position.scale();
		return position;
	}
}