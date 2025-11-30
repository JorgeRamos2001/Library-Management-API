package com.app.services;

import com.app.models.dtos.MemberDTO;
import com.app.models.requests.CreateMemberRequest;

import java.util.List;

public interface IMemberService {
    MemberDTO save(CreateMemberRequest member);
    MemberDTO findById(Long id);
    MemberDTO findByDui(String dui);
    MemberDTO findByEmail(String email);
    List<MemberDTO> findAllActiveMembers();
    List<MemberDTO> findAll();
    List<MemberDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    MemberDTO update(Long id, CreateMemberRequest member);
    void delete(Long id);
}
