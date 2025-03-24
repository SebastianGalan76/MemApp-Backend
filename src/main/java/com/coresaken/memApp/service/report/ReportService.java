package com.coresaken.memApp.service.report;

import com.coresaken.memApp.data.dto.NewReportDto;
import com.coresaken.memApp.data.response.Response;
import com.coresaken.memApp.database.model.Report;
import com.coresaken.memApp.database.model.User;
import com.coresaken.memApp.database.model.comment.Comment;
import com.coresaken.memApp.database.model.post.Post;
import com.coresaken.memApp.database.repository.ReportRepository;
import com.coresaken.memApp.database.repository.UserRepository;
import com.coresaken.memApp.database.repository.comment.CommentRepository;
import com.coresaken.memApp.database.repository.post.PostRepository;
import com.coresaken.memApp.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    final UserService userService;

    final ReportRepository reportRepository;

    final PostRepository postRepository;
    final UserRepository userRepository;
    final CommentRepository commentRepository;

    public ResponseEntity<Response> report(NewReportDto newReportDto, HttpServletRequest request) {
        Report report = new Report();
        report.setReason(newReportDto.getReason().substring(0, Math.min(newReportDto.getReason().length(), 255)));

        if(newReportDto.getReportedPostId() != null){
            Optional<Post> postOptional = postRepository.findById(newReportDto.getReportedPostId());
            if(postOptional.isEmpty()){
                return Response.badRequest(1, "Nie znaleziono posta o podanym ID!");
            }

            report.setReportedPost(postOptional.get());
            return report(report, request);
        }
        if(newReportDto.getReportedUserId() != null){
            Optional<User> userOptional = userRepository.findById(newReportDto.getReportedUserId());
            if(userOptional.isEmpty()){
                return Response.badRequest(2, "Nie znaleziono użytkownika o podanym ID!");
            }

            report.setReportedUser(userOptional.get());
            return report(report, request);
        }
        if(newReportDto.getReportedCommentId() != null){
            Optional<Comment> commentOptional = commentRepository.findById(newReportDto.getReportedCommentId());
            if(commentOptional.isEmpty()){
                return Response.badRequest(3, "Nie znaleziono komentarza o podanym ID!");
            }

            report.setReportedComment(commentOptional.get());
            return report(report, request);
        }

        return Response.badRequest(4, "Wystąpił nieoczekiwany błąd. Spróbuj ponownie później.");
    }

    private ResponseEntity<Response> report(Report report, HttpServletRequest request){
        User user = userService.getLoggedInUser();

        report.setUser(user);
        if(user == null){
            report.setUserIp(request.getRemoteAddr());
        }

        reportRepository.save(report);
        return Response.ok("Zgłoszenie zostało wysłane.");
    }
}
