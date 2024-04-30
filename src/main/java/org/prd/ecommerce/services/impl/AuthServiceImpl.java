package org.prd.ecommerce.services.impl;


import jakarta.annotation.PostConstruct;
import org.prd.ecommerce.config.util.enums.StatusType;
import org.prd.ecommerce.config.util.mapper.EntitiesMapper;
import org.prd.ecommerce.config.util.validation.ValidatingService;
import org.prd.ecommerce.controller.exceptions.services.DuplicateDataException;
import org.prd.ecommerce.entities.entity.UserEntity;
import org.prd.ecommerce.entities.dto.SignupRequest;
import org.prd.ecommerce.entities.dto.UserEntityDto;
import org.prd.ecommerce.config.util.enums.UserRole;
import org.prd.ecommerce.repository.UserEntityRepository;
import org.prd.ecommerce.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private EntitiesMapper userMapper;

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);



    @Override
    @Transactional
    public UserEntityDto createUser(SignupRequest signupRequest) {
        if(userEntityRepository.existsByEmail(signupRequest.getEmail())){
            logger.error(String.format("Error al crear el usuario '%s': Email already exists", signupRequest.getEmail()));
            throw new DuplicateDataException("email",signupRequest.getEmail(),StatusType.DUPLICATE_EMAIL);
        }
        if(userEntityRepository.existsByNickname(signupRequest.getNickname())){
            logger.error(String.format("Error al crear el usuario '%s': Nickname already exists", signupRequest.getNickname()));
            throw new DuplicateDataException("nickname",signupRequest.getNickname(), StatusType.DUPLICATE_NICKNAME);
        }
        UserEntityDto userEntityDto = userMapper.singupRequestToCustomerUserEntityDto(signupRequest);

        new ValidatingService<UserEntityDto>().validateInput(userEntityDto);

        UserEntity userEntity = userMapper.userEntityDtoToUserEntity(userEntityDto);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntityDto.getPassword()));

        Optional<UserEntity> createdUser = Optional.of(userEntityRepository.save(userEntity));

        return userMapper.userEntityToUserEntityDto(createdUser.get());

//        }catch (DataIntegrityViolationException e) {
//            // Manejar violaciones de integridad de la base de datos
//            logger.error(String.format("Error al crear el usuario: %s", e.getMessage()));
//            throw e;
//        } catch (DataAccessResourceFailureException e) {
//            // Manejar otra excepción específica
//            logger.error(String.format("Error al conectar a la base de datos: %s", e.getMessage()));
//            throw e;
//        } catch (Exception e) {
//            // Manejar excepciones generales
//            logger.error(String.format("Error inesperado: %s", e.getMessage()));
//            throw e;
//        }

    }
//    @PostConstruct
//    public void createAdminAccount() {
//        UserEntity admin = userEntityRepository.findByUserRole(UserRole.ADMIN);
//        if(admin == null){
//            UserEntity user = new UserEntity();
//            user.setNickname("admin123");
//            user.setEmail("admin123@gmail.com");
//            user.setPassword(bCryptPasswordEncoder.encode("Admin123"));
//            user.setUserRole(UserRole.ADMIN);
//            user.setEnabled(true);
//            userEntityRepository.save(user);
//        }
//    }






}
