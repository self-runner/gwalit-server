package com.selfrunner.gwalit.domain.board;

import com.selfrunner.gwalit.domain.board.dto.request.PostBoardReq;
import com.selfrunner.gwalit.domain.board.dto.response.BoardRes;
import com.selfrunner.gwalit.domain.board.entity.Board;
import com.selfrunner.gwalit.domain.board.repository.BoardRepository;
import com.selfrunner.gwalit.domain.board.repository.FileJdbcRepository;
import com.selfrunner.gwalit.domain.board.repository.FileRepository;
import com.selfrunner.gwalit.domain.board.repository.ReplyRepository;
import com.selfrunner.gwalit.domain.board.service.BoardService;
import com.selfrunner.gwalit.domain.lecture.entity.Lecture;
import com.selfrunner.gwalit.domain.member.entity.Member;
import com.selfrunner.gwalit.domain.member.entity.MemberAndLecture;
import com.selfrunner.gwalit.domain.member.repository.MemberAndLectureRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberAndNotificationJdbcRepository;
import com.selfrunner.gwalit.domain.member.repository.MemberRepository;
import com.selfrunner.gwalit.domain.notification.repository.NotificationRepository;
import com.selfrunner.gwalit.global.util.aws.S3Client;
import com.selfrunner.gwalit.global.util.fcm.FCMClient;
import com.selfrunner.gwalit.util.LectureTestUtil;
import com.selfrunner.gwalit.util.MemberAndLectureTestUtil;
import com.selfrunner.gwalit.util.MemberTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    private BoardService boardService;
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileJdbcRepository fileJdbcRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberAndLectureRepository memberAndLectureRepository;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private MemberAndNotificationJdbcRepository memberAndNotificationJdbcRepository;

    @Mock
    private S3Client s3Client;

    @Mock
    private FCMClient fcmClient;

    @BeforeEach
    void beforeEach() {
        boardService = new BoardService(boardRepository, replyRepository, fileRepository, fileJdbcRepository, memberRepository, memberAndLectureRepository, notificationRepository, memberAndNotificationJdbcRepository, s3Client, fcmClient);
    }

    @Test
    @DisplayName("게시글 등록 및 조회")
    void registerBoard() {
        // given
        Member member = MemberTestUtil.getMockMember();
        Lecture lecture = LectureTestUtil.getMockLecture();
        MemberAndLecture memberAndLecture = MemberAndLectureTestUtil.getMockMemberAndLecture(member, lecture);
        PostBoardReq postBoardReq = PostBoardReq.builder().build();
        Board board = postBoardReq.toEntity(lecture, member);
        given(boardRepository.save(any(Board.class))).willReturn(board);
        given(memberAndLectureRepository.findMemberAndLectureByMemberAndLectureLectureId(any(Member.class), any(Long.class))).willReturn(Optional.ofNullable(memberAndLecture));

        // when
        BoardRes boardRes = boardService.registerBoard(member, null, postBoardReq);

        // then
        assertThat(boardRes).isNotNull();
        assertThat(boardRes)
                .extracting("title", "body", "category", "status")
                .containsExactly(board.getTitle(), board.getBody(), board.getCategory(), board.getStatus());
    }
}
