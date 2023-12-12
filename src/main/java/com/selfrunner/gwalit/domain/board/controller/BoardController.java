package com.selfrunner.gwalit.domain.board.controller;

import com.selfrunner.gwalit.domain.board.dto.request.PostBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.PutBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.ReplyReq;
import com.selfrunner.gwalit.domain.board.dto.response.*;
import com.selfrunner.gwalit.domain.board.service.BoardService;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.common.ApplicationResponse;
import com.selfrunner.gwalit.global.exception.ErrorCode;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/board")
@Tag(name = "board", description = "질문 게시판 기능 API")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "질문 등록")
    @PostMapping("")
    public ApplicationResponse<BoardRes> registerBoard(@Auth Member member, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList, @Valid @RequestPart(value = "data") PostBoardReq postBoardReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.registerBoard(member, multipartFileList, postBoardReq));
    }

    @Operation(summary = "질문 수정")
    @PutMapping("/{board_id}")
    public ApplicationResponse<BoardRes> updateBoard(@Auth Member member, @PathVariable(value = "board_id") Long boardId, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList, @Valid @RequestPart(value = "data") PutBoardReq putBoardReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.updateBoard(member, boardId, multipartFileList, putBoardReq));
    }

    @Operation(summary = "질문 삭제")
    @DeleteMapping("/{board_id}")
    public ApplicationResponse<Void> deleteBoard(@Auth Member member, @PathVariable(value = "board_id") Long boardId) {
        boardService.deleteBoard(member, boardId);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "질문 상태 변경")
    @PatchMapping("/{board_id}")
    public ApplicationResponse<BoardRes> changeQuestionStatus(@Auth Member member, @PathVariable(value = "board_id") Long boardId) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.changeQuestionStatus(member, boardId));
    }

    @Operation(summary = "질문 1개 가져오기")
    @GetMapping("/{board_id}")
    public ApplicationResponse<BoardReplyRes> getOneBoard(@Auth Member member, @PathVariable(value = "board_id") Long boardId) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.getOneBoard(member, boardId));
    }

    @Operation(summary = "질문 페이지네이션")
    @GetMapping("/list/{lecture_id}")
    public ApplicationResponse<Slice<BoardMetaRes>> getBoardPagination(@Auth Member member, @PathVariable(value = "lecture_id") Long lectureId, @RequestParam(name = "category") String category, @RequestParam(name = "cursor", required = false) Long cursor, @PageableDefault(size = 15)Pageable pageable) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.getBoardPagination(member, lectureId, category, cursor, pageable));
    }

    @Operation(summary = "질문 게시 상태만 가져오기 (메인 페이지용)")
    @GetMapping("/main")
    public ApplicationResponse<List<BoardMetaRes>> getOpenQuestion(@Auth Member member) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.getOpenQuestion(member));
    }

    @Operation(summary = "댓글 등록")
    @PostMapping("/{board_id}/reply")
    public ApplicationResponse<ReplyRes> registerReply(@Auth Member member, @PathVariable(value = "board_id") Long boardId, @RequestPart(value = "file", required = false) List<MultipartFile> multipartFileList, @Valid @RequestPart(value = "data") ReplyReq replyReq) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.registerReply(member, boardId, multipartFileList, replyReq));
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{board_id}/reply/{reply_id}")
    public ApplicationResponse<Void> deleteReply(@Auth Member member, @PathVariable(value = "board_id") Long boardId, @PathVariable(value = "reply_id") Long replyId) {
        boardService.deleteReply(member, boardId, replyId);
        return ApplicationResponse.create(ErrorCode.SUCCESS);
    }

    @Operation(summary = "댓글 페이지네이션")
    @GetMapping("/{board_id}/reply/list")
    public ApplicationResponse<Slice<ReplyRes>> getReplyPagination(@Auth Member member, @PathVariable(value = "board_id") Long boardId, @RequestParam(name = "cursor", required = false) Long cursor, @PageableDefault(size = 15) Pageable pageable) {
        return ApplicationResponse.create(ErrorCode.SUCCESS, boardService.getReplyPagination(member, boardId, cursor, pageable));
    }

    @Operation(summary = "파일 용량 조회")
    @GetMapping("/capacity/{lecture_id}")
    public ApplicationResponse<BoardFileRes> getFileCapacity(@Auth Member member, @PathVariable(value = "lecture_id") Long lectureId) {
        return ApplicationResponse.ok(ErrorCode.SUCCESS, boardService.getFileCapacity(member, lectureId));
    }
}
