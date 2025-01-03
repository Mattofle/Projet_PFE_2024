package com.example.back.auth;

import com.example.back.company.models.Company;
import com.example.back.company.CompanyRepository;
import com.example.back.company.models.Companywithcredentials;
import com.example.back.user.UserRepository;
import com.example.back.user.User;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class AuthService {

    private final Algorithm jwtAlgorithm;
    private final JWTVerifier jwtVerifier;

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public AuthService(AuthenticationProperties properties , UserRepository userRepository,CompanyRepository companyRepository) {
        this.jwtAlgorithm = Algorithm.HMAC512(properties.getSecret());
        this.jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public String authenticate(String identifier, String password) {
        // Recherche d'abord dans les entreprises
        User user = userRepository.findByLogin(identifier);

        // Vérifie le mot de passe si un utilisateur a été trouvé
        if (user != null && BCrypt.checkpw(password,user.getPassword())) {
            return JWT.create().withIssuer("auth0").withClaim("userId", user.getUserId()).withClaim("isAdmin", user.isIsAdmin()).sign(jwtAlgorithm);
        }

        return null; // Échec de la connexion
    }

    public Integer register(String identifier, String password, boolean isAdmin) {

        User user = new User();
        user.setLogin(identifier);
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        user.setIsAdmin(isAdmin);
        userRepository.save(user);
        return user.getUserId();
    }


    public Integer registerCompany(Companywithcredentials companywithcredentials) {
        User user = new User();
        Company company = new Company();
        try {
            // Création de l'utilisateur
            user.setLogin(companywithcredentials.getLogin());
            user.setPassword(BCrypt.hashpw(companywithcredentials.getPassword(), BCrypt.gensalt()));
            user.setIsAdmin(false);
            userRepository.save(user);

            // Création de l'entreprise
            company.setCompany_id(user.getUserId());
            company.setName(companywithcredentials.getName());
            company.setAddress(companywithcredentials.getAddress());
            company.setPhoneNumber(companywithcredentials.getPhoneNumber());
            company.setHasInstalation(companywithcredentials.getHasInstalation());
            company.setOwnsInstalation(companywithcredentials.getOwnsInstalation());
            company.setWorkers(companywithcredentials.getWorkers());
            company.setHasProduct(companywithcredentials.getHasProduct());
            company.setCompany_id(user.getUserId());
            company.setOwnsInstalation(companywithcredentials.getOwnsInstalation());
            companyRepository.save(company);

            return company.getCompany_id();

        } catch (Exception e) {
            // Suppression de l'utilisateur en cas d'erreur
            userRepository.delete(user);
            throw e; // Relance l'exception pour signaler l'échec
        }
    }



    public boolean verifyAdmin(String token){
        try {
            boolean isAdmin = jwtVerifier.verify(token).getClaim("isAdmin").asBoolean();
            return isAdmin;
        }catch (JWTVerificationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean userExists(String login) {
        return userRepository.findByLogin(login) != null;
    }

    public Integer getUserId(String token){
        try {
            return jwtVerifier.verify(token).getClaim("userId").asInt();
        }catch (JWTVerificationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}