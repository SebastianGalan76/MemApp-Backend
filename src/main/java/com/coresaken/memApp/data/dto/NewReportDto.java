package com.coresaken.memApp.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewReportDto {
    Long reportedPostId;
    Long reportedUserId;
    Long reportedCommentId;

    String reason;
}
