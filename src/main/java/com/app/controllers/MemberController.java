package com.app.controllers;

import com.app.models.dtos.MemberDTO;
import com.app.models.requests.CreateMemberRequest;
import com.app.services.IMemberService;
import com.app.services.implementations.MemberServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final IMemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDTO> save(@Valid @RequestBody CreateMemberRequest member) {
        return new ResponseEntity<>(memberService.save(member), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(memberService.findById(id));
    }

    @GetMapping("/dui/{dui}")
    public ResponseEntity<MemberDTO> findByDui(@PathVariable String dui){
        return ResponseEntity.ok(memberService.findByDui(dui));
    }

    @GetMapping("/email")
    public ResponseEntity<MemberDTO> findByEmail(@RequestParam String email){
        return ResponseEntity.ok(memberService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> findAll(){
        return ResponseEntity.ok(memberService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<MemberDTO>> findAllActiveMembers(){
        return ResponseEntity.ok(memberService.findAllActiveMembers());
    }

    @GetMapping("/search")
    public ResponseEntity<List<MemberDTO>> findByName(@RequestParam String name){
        return ResponseEntity.ok(memberService.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> update(@PathVariable Long id, @Valid @RequestBody CreateMemberRequest member){
        return ResponseEntity.ok(memberService.update(id, member));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        memberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
