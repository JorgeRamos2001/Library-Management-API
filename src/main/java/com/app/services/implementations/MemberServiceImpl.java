package com.app.services.implementations;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.DuplicateResourceException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.MemberDTO;
import com.app.models.entities.Member;
import com.app.models.requests.CreateMemberRequest;
import com.app.repositories.IMemberRepository;
import com.app.services.IMemberService;
import com.app.utils.DuiValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements IMemberService {

    private final IMemberRepository memberRepository;

    @Override
    @Transactional
    public MemberDTO save(CreateMemberRequest member) {
        if(!DuiValidator.isValidDUI(member.getDui())) throw new BadRequestException("The DUI entered is invalid");
        if( memberRepository.existsByDui(member.getDui())) throw new DuplicateResourceException("DUI: " + member.getDui() +" already exists.");
        if( memberRepository.existsByEmail(member.getEmail())) throw new DuplicateResourceException("Email: " + member.getEmail() +" already exists.");

        return mapToDTO(memberRepository.save(Member
                .builder()
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .dui(member.getDui())
                .email(member.getEmail())
                .createdOn(LocalDateTime.now())
                .isActive(true)
                .build())
        );
    }

    @Override
    public MemberDTO findById(Long id) {
        return mapToDTO(memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + id + " not found.")));
    }

    @Override
    public MemberDTO findByDui(String dui) {
        if(!DuiValidator.isValidDUI(dui)) throw new BadRequestException("The DUI entered is invalid");
        return mapToDTO(memberRepository.findByDui(dui).orElseThrow(() -> new ResourceNotFoundException("Member with DUI: " + dui + " not found.")));
    }

    @Override
    public MemberDTO findByEmail(String email) {
        return mapToDTO(memberRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Member with email: " + email + " not found.")));
    }

    @Override
    public List<MemberDTO> findAllActiveMembers() {
        return memberRepository.findAllByIsActive(true).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<MemberDTO> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName) {
        return memberRepository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(firstName, lastName).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public MemberDTO update(Long id, CreateMemberRequest member) {
        Member existingMember = memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + id + " not found."));

        if (!existingMember.getEmail().equals(member.getEmail())) {
            if(memberRepository.existsByEmail(member.getEmail())) throw new DuplicateResourceException("Email: " + member.getEmail() +" already exists.");
        }

        if(!DuiValidator.isValidDUI(member.getDui())) throw new BadRequestException("The DUI entered is invalid");
        if (!existingMember.getDui().equals(member.getDui())) {
            if(memberRepository.existsByDui(member.getDui())) throw new DuplicateResourceException("DUI: " + member.getDui() +" already exists.");
        }
        existingMember.setFirstName(member.getFirstName());
        existingMember.setLastName(member.getLastName());
        existingMember.setDui(member.getDui());
        existingMember.setEmail(member.getEmail());

        return mapToDTO(memberRepository.save(existingMember));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Member existingMember = memberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + id + " not found."));
        existingMember.setActive(false);
        memberRepository.save(existingMember);
    }

    private MemberDTO mapToDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .dui(member.getDui())
                .email(member.getEmail())
                .createdOn(member.getCreatedOn())
                .isActive(member.isActive())
                .build();
    }
}
