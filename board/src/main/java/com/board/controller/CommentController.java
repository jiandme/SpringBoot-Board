package com.board.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.board.adapter.GsonLocalDateTimeAdapter;
import com.board.domain.CommentDTO;
import com.board.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@RestController
public class CommentController {
	
	@Autowired
	private CommentService commentService;
	
	@GetMapping(value = "/comments/{boardIdx}")
	public JsonObject getCommentList(@PathVariable("boardIdx") Long boardIdx, @ModelAttribute("params") CommentDTO params) {
		
		JsonObject jsonObj = new JsonObject();
		
		List<CommentDTO> commentList = commentService.getCommentList(params);
		if (CollectionUtils.isEmpty(commentList) == false) {
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
			JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
			jsonObj.add("commentList",jsonArr);
		}
	
		return jsonObj;
	}
	
	@RequestMapping(value = { "/comments", "/comments/{idx}" }, method = { RequestMethod.POST, RequestMethod.PATCH })
	public JsonObject registerComment(@PathVariable(value = "idx", required = false) Long idx, @RequestBody final CommentDTO params) {

		JsonObject jsonObj = new JsonObject();

		try {

			boolean isRegistered = commentService.registerComment(params);
			jsonObj.addProperty("result", isRegistered);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "?????????????????? ?????? ????????? ????????? ?????????????????????.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "???????????? ????????? ?????????????????????.");
		}

		return jsonObj;
	}
	
	@DeleteMapping(value = "/comments/{idx}")
	public JsonObject deleteComment(@PathVariable("idx") final Long idx) {

		JsonObject jsonObj = new JsonObject();


		try {
			boolean isDeleted = commentService.deleteComment(idx);
			jsonObj.addProperty("result", isDeleted);

		} catch (DataAccessException e) {
			jsonObj.addProperty("message", "?????????????????? ?????? ????????? ????????? ?????????????????????.");

		} catch (Exception e) {
			jsonObj.addProperty("message", "???????????? ????????? ?????????????????????.");
		}

		return jsonObj;
	}
}
