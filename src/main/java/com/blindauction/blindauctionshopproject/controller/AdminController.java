package com.blindauction.blindauctionshopproject.controller;

import com.blindauction.blindauctionshopproject.dto.security.StatusResponse;
import com.blindauction.blindauctionshopproject.dto.admin.AdminSignupRequest;
import com.blindauction.blindauctionshopproject.dto.admin.SellerDetailResponse;
import com.blindauction.blindauctionshopproject.dto.admin.SellerPermissionResponse;
import com.blindauction.blindauctionshopproject.dto.admin.UserResponse;
import com.blindauction.blindauctionshopproject.service.AdminService;
import com.blindauction.blindauctionshopproject.util.jwtUtil.JwtUtil;
import com.blindauction.blindauctionshopproject.util.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final JwtUtil jwtUtil;
    private final AdminService adminService;

    @PostMapping("/signup") // 관리자 회원가입
    public ResponseEntity<StatusResponse> signupAdmin(@RequestBody @Valid AdminSignupRequest adminSignupRequest) { // @Valid : @pattern 등 값제한 어노테이션 사용 위해 필요
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "관리자 회원가입 완료"); // httpStatus.0000 들은 안에 int value , httpstatus.series series, String reasonPhrase 필드 3개 있는데 그중 value( 500, 200 ) 만 가져오는거
        HttpHeaders headers = new HttpHeaders(); //httpHeader 란 ? 클라이언트 - 서버 간 요청 또는 응답에 부가적 정보를 주고받을 수 있는 문자열
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8)); // 위에서 만든 헤더에 담을 Content 의 type 을 정의

        adminService.signupAdmin(adminSignupRequest); // userService 에서 회원가입 기능 작동
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.CREATED);
    }

    @GetMapping("/users") // 회원목록 조회
    public Page<UserResponse> getUserList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getUserList(userDetails.getUser().getId());
    }

    @GetMapping("/sellers") // 판매자목록 조회
    public Page<SellerDetailResponse> getSellerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getSellerList(userDetails.getUser().getId());
    }

    @GetMapping("/seller-permissions") // 판매자권한 요청목록 조회
    public Page<SellerPermissionResponse> getSellerPermissionList() {
        return adminService.getSellerPermissionList();
    }

    @PutMapping("/role/{userId}") // 판매자 권한 승인
    public ResponseEntity<StatusResponse> acceptSellerRole(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long userId) {
        adminService.acceptSellerRole(userId, userDetails.getUser());
        return ResponseEntity.accepted().body(new StatusResponse(HttpStatus.ACCEPTED.value(), "권한 승인"));
    }

    @PutMapping("/role/delete/{userId}") // 판매자 권한 삭제
    public ResponseEntity<StatusResponse> deleteSellerRole(@PathVariable Long userId) {
        adminService.deleteSellerRole(userId);
        return ResponseEntity.accepted().body(new StatusResponse(HttpStatus.ACCEPTED.value(), "권한 삭제"));
    }

    //관리자 로그인

    //관리자 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<StatusResponse> logoutAdmin(HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그아웃 완료");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, "");
        return new ResponseEntity<>(statusResponse, headers, HttpStatus.OK);
    }
}
