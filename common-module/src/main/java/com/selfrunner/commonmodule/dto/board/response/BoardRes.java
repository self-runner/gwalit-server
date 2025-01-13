package com.selfrunner.commonmodule.dto.board.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.selfrunner.commonmodule.enumerate.board.BoardCategory;
import com.selfrunner.commonmodule.enumerate.board.QuestionStatus;
import com.selfrunner.commonmodule.enumerate.member.MemberType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardRes {

    private final Long boardId;

    private final Long lectureId;

    private final Long memberId;

    private final MemberType memberType;

    private final String memberName;

    private final Boolean isPublic;

    private final Long lessonId;

    private final LocalDate lessonDate;

    private final String title;

    private final String body;

    private final BoardCategory category;

    private final QuestionStatus status;

    private final List<FileRes> fileList;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
