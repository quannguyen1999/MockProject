package com.main.controller;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.main.entity.Account;
import com.main.entity.QReport;
import com.main.entity.Report;
import com.main.model.Greeting;
import com.main.model.HelloMessage;
import com.main.model.ReportGetDto;
import com.main.model.ReportPostDto;
import com.main.service.ReportService;
import com.main.service.ResponseService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@Controller
public class GreetingController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private ResponseService responseService;

	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public Greeting greeting(HelloMessage message) throws Exception {
		Thread.sleep(1000); // simulated delay
		return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
	}

	@MessageMapping("/websocket/report")
	@SendTo("/topic/websocket/topic/reports")
	public Object countReport(//HelloMessage helloMessage,
			//			@RequestParam Optional<Integer> page,
			//			@RequestParam Optional<Integer> size
			@RequestBody String str
			) throws Exception{
		JsonObject data = new Gson().fromJson(str, JsonObject.class);
		String names = data .get("name").getAsString();
		reportService.create(new ReportPostDto(names));
		return responseService.getResponseCustom("request.getListSuccess",reportService.getNewReport(), HttpStatus.OK);
	}
}
