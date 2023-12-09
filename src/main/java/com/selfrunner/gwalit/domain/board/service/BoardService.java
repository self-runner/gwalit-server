package com.selfrunner.gwalit.domain.board.service;

import com.selfrunner.gwalit.domain.board.dto.request.PostBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.PutBoardReq;
import com.selfrunner.gwalit.domain.board.dto.request.ReplyReq;
import com.selfrunner.gwalit.domain.board.dto.response.BoardMetaRes;
import com.selfrunner.gwalit.domain.board.dto.response.BoardReplyRes;
import com.selfrunner.gwalit.domain.board.dto.response.BoardRes;
import com.selfrunner.gwalit.domain.board.dto.response.ReplyRes;
import com.selfrunner.gwalit.domain.board.repository.BoardRepository;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import com.selfrunner.gwalit.global.util.jwt.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final S3Client s3Client;

    public BoardRes registerBoard(Member member, List<MultipartFile> multipartFileList, PostBoardReq postBoardReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public BoardRes updateBoard(Member member, Long boardId, List<MultipartFile> multipartFileList, PutBoardReq putBoardReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public void deleteBoard() {
        // Validation

        // Business Logic

        // Response
    }

    public BoardRes changeQuestionStatus(Member member, Long boardId) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public BoardReplyRes getOneBoard(@Auth Member member, Long boardId) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public Slice<BoardMetaRes> getBoardPagination(Member member, String category, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public List<BoardRes> getOpenQuestion(Member member) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public ReplyRes registerReply(Member member, Long boardId, List<MultipartFile> multipartFileList, ReplyReq replyReq) {
        // Validation

        // Business Logic

        // Response
        return null;
    }

    public void deleteReply(Member member, Long boardId, Long replyId) {
        // Validation

        // Business Logic

        // Response
    }

    public Slice<ReplyRes> getReplyPagination(Member member, Long boardId, Long cursor, Pageable pageable) {
        // Validation

        // Business Logic

        // Response
        return null;
    }
}
