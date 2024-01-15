package org.prd.ecommerce.services.impl;


import jakarta.annotation.PostConstruct;
import org.prd.ecommerce.entities.UserEntity;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.enums.UserRole;
import org.prd.ecommerce.repository.UserEntityRepository;
import org.prd.ecommerce.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
@Service

public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private  BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);



    @Override
    @Transactional
    public Optional<UserEntityDto> createUser(SignupRequest signupRequest) {
        try{
        UserEntity userEntity = new UserEntity();
        userEntity.setNickname(signupRequest.getNickname());
        userEntity.setEmail(signupRequest.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(signupRequest.getPassword()));
        userEntity.setUserRole(UserRole.CUSTOMER);
        userEntity.setEnabled(true);
        Optional<UserEntity> createdUser = Optional.of(userEntityRepository.save(userEntity));
        UserEntityDto userEntityDto = new UserEntityDto();
        userEntityDto.setId(createdUser.get().getId());
        userEntityDto.setNickname(createdUser.get().getNickname());
        userEntityDto.setEmail(createdUser.get().getEmail());
        userEntityDto.setUserRole(createdUser.get().getUserRole());
        userEntityDto.setEnabled(createdUser.get().isEnabled());
        logger.info("User created successfully");
        return Optional.of(userEntityDto);

        }catch (DataIntegrityViolationException e) {
            // Manejar violaciones de integridad de la base de datos
            logger.error(String.format("Error al crear el usuario: %s", e.getMessage()));
            throw e;
        } catch (DataAccessResourceFailureException e) {
            // Manejar otra excepción específica
            logger.error(String.format("Error al conectar a la base de datos: %s", e.getMessage()));
            throw e;
        } catch (Exception e) {
            // Manejar excepciones generales
            logger.error(String.format("Error inesperado: %s", e.getMessage()));
            throw e;
        }

    }

    public void createAdminAccount() {
        UserEntity admin = userEntityRepository.findByUserRole(UserRole.ADMIN);
        if(admin == null){
            UserEntity user = new UserEntity();
            user.setNickname("admin123");
            user.setEmail("admin123@gmail.com");
            user.setPassword(bCryptPasswordEncoder.encode("Admin123"));
            user.setUserRole(UserRole.ADMIN);
            user.setEnabled(true);
            userEntityRepository.save(user);
        }
    }





}
