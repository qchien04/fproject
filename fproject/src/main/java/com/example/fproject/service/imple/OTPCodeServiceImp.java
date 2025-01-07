package com.example.fproject.service.imple;


import com.example.fproject.entity.OTPCode;
import com.example.fproject.repository.OTPCodeRepo;
import com.example.fproject.service.OTPCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class OTPCodeServiceImp implements OTPCodeService {

    @Autowired
    private OTPCodeRepo otpCodeRepo;

    @Override
    public OTPCode findOTPCode(String mail, String data) {
        Optional<OTPCode> otpCode =otpCodeRepo.findByMailAndData(mail, data);

        if (otpCode.isPresent()) {
            return otpCode.get();
        }
        return null;
    }

    @Override
    public void saveOTPCode(OTPCode otpCode) {
        OTPCode otp=otpCodeRepo.save(otpCode);
    }
}
