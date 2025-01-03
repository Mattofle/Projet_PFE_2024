package com.example.back;


import com.example.back.answerCompany.AnswerCompanyService;
import com.example.back.answerCompany.models.AnswersCompany;
import com.example.back.answerCompany.models.CompanyAnswer;
import com.example.back.commentCompany.CommentCompany;
import com.example.back.commentCompany.CommentCompanyService;
import com.example.back.commentCompany.CommentDTO;
import com.example.back.company.CompanyService;
import com.example.back.company.models.Company;
import com.example.back.company.models.Companywithcredentials;
import com.example.back.exceptions.ConflictException;
import com.example.back.exceptions.NotFoundException;
import com.example.back.exceptions.UnauthoziedExcepetion;
import com.example.back.module.Module;
import com.example.back.module.ModuleAllInfos;
import com.example.back.module.ModuleService;
import com.example.back.moduleCompany.ModuleCompany;
import com.example.back.moduleCompany.ModuleCompanyService;
import com.example.back.moduleCompany.ModuleCompanyWithSubmodules;
import com.example.back.scoring.ScoringEsgService;
import com.example.back.scoring.ScoringService;
import com.example.back.user.User;
import com.example.back.Answer.Answer;
import com.example.back.Answer.AnswerService;
import com.example.back.answerESG.AnswerESG;
import com.example.back.answerESG.AnswerESGService;
import com.example.back.answerODD.AnswerODD;
import com.example.back.answerODD.AnswerODDService;
import com.example.back.glossaire.Glossaire;
import com.example.back.glossaire.GlossaireService;
import com.example.back.question.Question;
import com.example.back.question.QuestionService;
import com.example.back.auth.AuthService;
import com.example.back.auth.Credientials;
import com.example.back.user.UserService;
import jakarta.persistence.criteria.CriteriaBuilder;
import com.example.back.user.UserWithoutCred;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Controller class for handling backend API requests.
 * This class provides various endpoints for managing authentication,
 * users, companies, answers, questions, modules, comments, and scoring.
 * It supports CRUD operations and additional business logic for these entities.
 */
@RestController
@RequestMapping("/api")
public class BackController {

  private final AuthService authService;
  private final QuestionService questionService;

  private final AnswerESGService answerESGService;

  private final  AnswerODDService answerODDService;

  private final AnswerService answerService;

  private final GlossaireService glossaireService;

  private final UserService userService;

  private final CompanyService companyService;

  private final ModuleService moduleService;

  private final AnswerCompanyService answersCompanyService;

  private final ModuleCompanyService moduleCompanyService;

  private final ScoringService scoringService;

  private final ScoringEsgService scoringEsgService;

  private final CommentCompanyService commentCompanyService;

  public BackController(AuthService service, QuestionService questionService,
      AnswerESGService answerESGService, AnswerODDService answerODDService,
      AnswerService answerService, GlossaireService glossaireService, UserService userService,
      CompanyService companyService, AnswerCompanyService answersCompanyService,
      ModuleService moduleService, ModuleCompanyService moduleCompanyService,
      ScoringService scoringService, ScoringEsgService scoringEsgService,
      CommentCompanyService commentCompanyService) {

    this.authService = service;
    this.questionService = questionService;
    this.answerESGService = answerESGService;
    this.answerODDService = answerODDService;
    this.answerService = answerService;
    this.glossaireService = glossaireService;
    this.userService = userService;
    this.companyService = companyService;
    this.moduleService = moduleService;
    this.answersCompanyService = answersCompanyService;
    this.moduleCompanyService = moduleCompanyService;
    this.scoringService = scoringService;
    this.scoringEsgService = scoringEsgService;
    this.commentCompanyService = commentCompanyService;
  }

  @GetMapping("/data")
  public ResponseEntity<String> getData() {
    return ResponseEntity.ok("Hello from Spring Boot!");
  }


  /**
   * Endpoint to submit a company answer.
   *
   * @param companyId The ID of the company.
   * @param answer    The answer data.
   * @param token     Authorization token.
   * @throws UnauthoziedExcepetion If the user is unauthorized.
   * @throws ConflictException     If an answer already exists.
   */
  @PostMapping("/answerCompany/companies/{companyId}")
  public void answerQuestion(@PathVariable int companyId, @RequestBody CompanyAnswer answer,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId != companyId && !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (answer == null || answer.getQuestionId() == 0) {
      throw new IllegalArgumentException("Information manquante.");
    }

    AnswersCompany answerCompany = answersCompanyService.getAnswerCompany(answer, companyId);
    if (answerCompany != null) {
      String message = "Vous avez déjà répondu à cette question.";
      throw new ConflictException(message);
    }

    if(answersCompanyService.moduleValidated(answer, companyId)){
      String message = "Vous ne pouvez pas modifier l'engagement d'une question d'un module validé.";
      throw new UnauthoziedExcepetion(message);
    }

    answersCompanyService.saveAnswerCompany(answer, companyId);
  }


  @DeleteMapping("/answerCompany/companies/{companyId}")
  public void deleteAnswer(@PathVariable int companyId, @RequestBody CompanyAnswer answer,
      @RequestHeader("Authorization") String token) {
    if (token == null) {

      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {

      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if (userId != companyId && !authService.verifyAdmin(token)) {

      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }


    if (answer == null || answer.getQuestionId() == 0) {

      throw new IllegalArgumentException("Information manquante.");
    }

    AnswersCompany answerCompany = answersCompanyService.getAnswerCompany(answer, companyId);
    if (answerCompany == null) {

      String message = "Vous n'avez pas encore répondu à cette question.";
      throw new NotFoundException(message);

    }

    if(answersCompanyService.moduleValidated(answer, companyId)){

      String message = "Vous ne pouvez pas modifier l'engagement d'une question d'un module validé.";
      throw new UnauthoziedExcepetion(message);
    }

    answersCompanyService.deleteAnswerCompany(answer, companyId);
  }

  /**
   * AnswerESG
   */

  /**
   * AnswerODD
   */

  /**
   * Authentication
   */



  /**
   * Endpoint to authenticate a user.
   *
   * @param credientials The user's login credentials.
   * @return A JWT token if authentication is successful.
   * @throws UnauthoziedExcepetion If the login or password is incorrect.
   * @throws IllegalArgumentException If the credentials are missing.
   */
  @PostMapping("/authentication/login")
  public String authenticate(@RequestBody Credientials credientials) {
    if (credientials == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    String identifier = credientials.getLogin();
    String password = credientials.getPassword();
    String userToken = authService.authenticate(identifier, password);

    if (userToken != null) {
      String message =
          authService.verifyAdmin(userToken) ? "Connexion réussie pour un administrateur"
              : "Connexion réussie pour une entreprise";
      return userToken;
    }
    throw new UnauthoziedExcepetion("Identifiant ou mot de passe incorrect.");
  }

  /**
   * Endpoint to register a new user.
   *
   * @param credientials The user's login credentials.
   * @return The ID of the new user.
   * @throws UnauthoziedExcepetion If the user is not authorized.
   * @throws IllegalArgumentException If the credentials are missing.
   * @throws NullPointerException     If the login or password is missing.
   * @throws ConflictException       If the user already exists.
   */
  @PostMapping("/authentication/register/admin")
  public int createUser(@RequestBody Credientials credientials,
      @RequestHeader("Authorization") String token) {
    if (token == null || !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (credientials == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    if (credientials.getLogin() == null || credientials.getPassword() == null) {
      String message = "Veuillez fournir un identifiant et un mot de passe.";
      throw new NullPointerException(message);
    }

    if (authService.userExists(credientials.getLogin())) {
      String message = "Un utilisateur avec cet identifiant existe déjà.";
      throw new ConflictException(message);
    }

    Integer userId = authService.register(credientials.getLogin(), credientials.getPassword(),
        credientials.isIs_admin());

    return userId;
  }

  /**
   * Endpoint to delete a user.
   *
   * @param id    The ID of the user to delete.
   * @param token Authorization token.
   * @return A response indicating whether the user was deleted.
   * @throws UnauthoziedExcepetion   If the user is unauthorized.
   * @throws ResponseStatusException If the user is not found or not an admin.
   */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable int id,
      @RequestHeader("Authorization") String token) {

    User user = userService.getUserById(id);


    if (token == null || !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    if (!user.isIsAdmin()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    if (userService.deleteUser(id)) {
      return ResponseEntity.ok("User deleted");
    }
    return ResponseEntity.ok("User not deleted");
  }

  /**
   * Endpoint to register a new company user.
   *
   * @param company The company's login credentials and details.
   * @param token   Authorization token.
   * @return The ID of the new company user.
   * @throws UnauthoziedExcepetion     If the user is not authorized.
   * @throws IllegalArgumentException  If the company data is missing.
   * @throws NotFoundException         If login or password is missing.
   * @throws ConflictException         If a user with the same login already exists.
   */
  @PostMapping("/authentication/register/company")
  public int createCompagny(@RequestBody Companywithcredentials company,
      @RequestHeader("Authorization") String token) {
    if (token == null || !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (company == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    if (company.getLogin() == null || company.getPassword() == null) {
      String message = "Veuillez fournir un identifiant et un mot de passe.";
      throw new NotFoundException(message);
    }

    if (authService.userExists(company.getLogin())) {
      String message = "Un utilisateur avec cet identifiant existe déjà.";
      throw new ConflictException(message);
    }
    Integer userId = authService.registerCompany(company);

    return userId;
  }

  /**
   * Company
   */

  /**
   * Endpoint to retrieve a list of all companies.
   *
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link Company}.
   */
  @GetMapping("/companies")
  public ResponseEntity<Iterable<Company>> getAllCompanies() {
    return ResponseEntity.ok(companyService.getCompanies());
  }

  /**
   * Endpoint to retrieve a specific company.
   *
   * @param companyId The ID of the company.
   * @param token     Authorization token.
   * @return The company details.
   * @throws UnauthoziedExcepetion If the user is unauthorized.
   */
  @GetMapping("/companies/{companyId}")
  public ResponseEntity<Company> getCompany(@PathVariable int companyId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId != companyId) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Company company = companyService.getCompany(companyId);

    if (company == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(company);
  }

  /**
   * Endpoint to update a company's details.
   *
   * @param companyId The ID of the company to update.
   * @param company   The updated company data.
   * @param token     Authorization token.
   * @return The updated company.
   * @throws UnauthoziedExcepetion    If the user is unauthorized.
   * @throws IllegalArgumentException If the company data is missing.
   */
  @PatchMapping("/companies/{companyId}")
  public Company updateCompany(@PathVariable int companyId, @RequestBody Company company,
      @RequestHeader("Authorization") String token) {
    if (token == null) {

      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (company == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    Integer userId = authService.getUserId(token);

    if (companyId != userId) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return companyService.updateCompany(company, companyId);
  }

  /**
   * Endpoint to delete a company.
   *
   * @param companyId The ID of the company to delete.
   * @param token     Authorization token.
   * @throws UnauthoziedExcepetion If the user is unauthorized.
   */
    @DeleteMapping("/companies/{companyId}")
    public void deleteCompany(@PathVariable int companyId, @RequestHeader("Authorization") String token) {
        if (token == null) {
            String message = "Vous n'êtes pas autorisé à effectuer cette action.";
            throw new UnauthoziedExcepetion(message);
        }


        if ( !authService.verifyAdmin(token)&& companyId != authService.getUserId(token)) {
            String message = "Vous n'êtes pas autorisé à effectuer cette action.";
            throw new UnauthoziedExcepetion(message);
        }

        companyService.deleteCompany(companyId);
    }

  /**
   * From
   */

  /**
   * Glossaire
   */


  /**
   * Endpoint to retrieve a list of glossaries.
   *
   * @param token Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link Glossaire}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/glossaires")
  public ResponseEntity<Iterable<Glossaire>> getGlossaire(
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(glossaireService.getGlossaires());
  }

  /**
   * Module
   */

  /**
   * ModuleAllInfos
   */

  /**
   * Endpoint to retrieve detailed information about ESG (Environmental, Social, and Governance) modules for a specific company.
   *
   * @param companyId The ID of the company for which to retrieve the ESG module information.
   * @param token     Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing a {@link ModuleAllInfos} object with modules, questions, answers, company-specific answers, and comments related to ESG.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized to access the information.
   */
  @GetMapping("/moduleAllInfos/ESG/{companyId}")
  public ResponseEntity<ModuleAllInfos> getModulesESG(@PathVariable int companyId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    Company company = companyService.getCompany(companyId);

    Iterable<Module> modules = moduleService.getModulesEsg();
    Iterable<Question> questions = questionService.getQuestionsByModules(modules, company);
    Iterable<AnswerESG> answerESGS = answerESGService.getAnswersESGByQuestions(questions, company);
    Iterable<Answer> answers = answerService.getAnswersByAnswerESG(answerESGS);
    Iterable<AnswersCompany> answersCompanies = answersCompanyService.getAnswersCompanyByAnswerESGS(
        answerESGS, companyId);
    Iterable<CommentCompany> commentCompanies = commentCompanyService.getAllByCompany(
        company.getCompany_id());

    ModuleAllInfos moduleAllInfos = new ModuleAllInfos();
    moduleAllInfos.setModules(modules);
    moduleAllInfos.setQuestions(questions);
    moduleAllInfos.setAnswerESGS(answerESGS);
    moduleAllInfos.setAnswers(answers);
    moduleAllInfos.setCompanyAnswers(answersCompanies);
    moduleAllInfos.setCommentCompanies(commentCompanies);

    return ResponseEntity.ok(moduleAllInfos);
  }

  /**
   * Endpoint to retrieve detailed information about ODD (Objectifs de Développement Durable) modules for a specific company.
   *
   * @param companyId The ID of the company for which to retrieve the ODD module information.
   * @param token     Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing a {@link ModuleAllInfos} object with modules, questions, answers, and company-specific answers related to ODD.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/moduleAllInfos/ODD/{companyId}")
  public ResponseEntity<ModuleAllInfos> getModulesODD(@PathVariable int companyId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    Company company = companyService.getCompany(companyId);

    Iterable<Module> modules = moduleService.getModulesOdd();
    Iterable<Question> questions = questionService.getQuestionsByModules(modules, company);
    Iterable<AnswerODD> answerODDS = answerODDService.getAnswersODDByQuestions(questions);
    Iterable<Answer> answers = answerService.getAnswersByAnswerODD(answerODDS);
    Iterable<AnswersCompany> answersCompanies = answersCompanyService.getAnswersCompanyByAnswerODDS(
        answerODDS, companyId);

    ModuleAllInfos moduleAllInfos = new ModuleAllInfos();
    moduleAllInfos.setModules(modules);
    moduleAllInfos.setQuestions(questions);
    moduleAllInfos.setAnswerODDS(answerODDS);
    moduleAllInfos.setAnswers(answers);
    moduleAllInfos.setCompanyAnswers(answersCompanies);



    return ResponseEntity.ok(moduleAllInfos);
  }

  /**
   * ModuleCompany
   */

  /**
   * Endpoint to retrieve the ODD (Objectifs de Développement Durable) score for a specific company.
   *
   * @param companyId The ID of the company for which to retrieve the ODD score.
   * @param token     Authorization token for verifying the user's permissions.
   * @return An {@link Integer} representing the calculated ODD score for the specified company.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized to access the ODD score.
   */
  @GetMapping("/companies/{companyId}/scoreodd")
  public Integer getOddScoreCompany(@PathVariable int companyId, @RequestHeader("Authorization") String token) {

    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if (companyId != authService.getUserId(token) && !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return scoringService.calculateModulesODDScoresForCompany(companyId);
  }

  /**
   * Endpoint to retrieve a module and its submodules for a specific company.
   *
   * @param companyId   The ID of the company for which to retrieve the module and submodules.
   * @param moduleTitle The title of the module to retrieve.
   * @param token       Authorization token for verifying the user's permissions.
   * @return A {@link ModuleCompanyWithSubmodules} containing the module and its associated submodules.
   * @throws UnauthoziedExcepetion    If the token is missing or the user is not authorized.
   * @throws IllegalArgumentException If the module title is missing.
   */
  @GetMapping("/moduleCompanyWithSubmodules/{moduleTitle}/company/{companyId}")
  public ModuleCompanyWithSubmodules getModulesCompany(@PathVariable int companyId,
      @PathVariable String moduleTitle, @RequestHeader("Authorization") String token) {

    if (token == null) {
        String message = "Vous n'êtes pas autorisé à effectuer cette action.";
        throw new UnauthoziedExcepetion(message);
    }

    if (companyId != authService.getUserId(token)) {
        String message = "Vous n'êtes pas autorisé à effectuer cette action.";
        throw new UnauthoziedExcepetion(message);
    }

    if (moduleTitle == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    return moduleCompanyService.getModuleTreeForCompany(companyId, moduleTitle);
  }

  /**
   * Endpoint to retrieve the ESG (Environmental, Social, and Governance) score for a specific company.
   *
   * @param companyId The ID of the company for which to retrieve the ESG score.
   * @param token     Authorization token for verifying the user's permissions.
   * @return A {@link Map} where the keys are categories or module names, and the values are lists of {@link Double} representing the ESG scores.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized to access the ESG score.
   */
  @GetMapping("/companies/{companyId}/scoreesg")
  public Map<String, List<Double>> getEsgScoreCompany(@PathVariable int companyId,
      @RequestHeader("Authorization") String token) {

    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if (companyId != authService.getUserId(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    return scoringEsgService.calculateModulesESGScoresForCompany(companyId);
  }

  /**
   * Endpoint to retrieve engagement details for a specific company.
   *
   * @param companyId The ID of the company for which to retrieve engagement details.
   * @param token     Authorization token for verifying the user's permissions.
   * @return A {@link Map} containing engagement details, where each entry maps a string key to a list of maps with string values.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/companies/{companyId}/answer/engagement")
  public Map<String, List<Map<String, String>>> getEngagement(@PathVariable int companyId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if (companyId != authService.getUserId(token) && !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    return answersCompanyService.getEngagement(companyId);
  }

  /**
   * Endpoint to add dates to a company module.
   *
   * @param moduleTitle The title of the module.
   * @param token       Authorization token.
   * @throws UnauthoziedExcepetion If the user is unauthorized.
   */
  @PostMapping("/companyModule/{moduleTitle}/dates")
  public void moduleAddDates(@PathVariable String moduleTitle ,@RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    moduleCompanyService.addDates(userId,moduleTitle);
  }

  /**
   * Question
   */

  /**
   * Endpoint to retrieve a list of all questions.
   *
   * @param token Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link Question}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/questions")
  public ResponseEntity<Iterable<Question>> getQuestion(
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(questionService.getQuestions());
  }

  /**
   * Endpoint to retrieve a list of answers for a specific question.
   *
   * @param questionId The ID of the question for which to retrieve the answers.
   * @param token      Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link Answer}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/questions/{questionId}/answers")
  public ResponseEntity<Iterable<Answer>> getAnswer(@PathVariable int questionId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(answerService.getAnswersByQuestion(questionId));
  }


  /**
   * Endpoint to retrieve a list of ESG answers for a specific question.
   *
   * @param questionId The ID of the question for which to retrieve the ESG answers.
   * @param token      Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link AnswerESG}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/questions/{questionId}/answersESG")
  public ResponseEntity<Iterable<AnswerESG>> getAnswerESG(@PathVariable int questionId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(answerESGService.getAnswersESGByQuestion(questionId));
  }


  /**
   * Endpoint to retrieve a list of ODD answers for a specific question.
   *
   * @param questionId The ID of the question for which to retrieve the ODD answers.
   * @param token      Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link AnswerODD}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not authorized.
   */
  @GetMapping("/questions/{questionId}/answersODD")
  public ResponseEntity<Iterable<AnswerODD>> getAnswerODD(@PathVariable int questionId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(answerODDService.getAnswersODDByQuestion(questionId));
  }

  /**
   * User
   */

  /**
   * Endpoint to retrieve a list of all administrators.
   *
   * @param token Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link UserWithoutCred}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not an administrator.
   */
  @GetMapping("/users/admin")
  public ResponseEntity<Iterable<UserWithoutCred>> getAdmins(
      @RequestHeader("Authorization") String token) {
    if (token == null || !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(userService.getAdmins());
  }

  /**
   * Endpoint to retrieve a list of all users.
   *
   * @param token Authorization token for verifying the user's permissions.
   * @return A {@link ResponseEntity} containing an {@link Iterable} of {@link UserWithoutCred}.
   * @throws UnauthoziedExcepetion If the token is missing or the user is not an administrator.
   */
  @GetMapping("/users")
  public ResponseEntity<Iterable<UserWithoutCred>> getUsers(
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null || !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    return ResponseEntity.ok(userService.getUsers());
  }

  @GetMapping("/users/token")
  public ResponseEntity<UserWithoutCred> getUser(@RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    int userId = authService.getUserId(token);
    User user = userService.getUserById(userId);

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return ResponseEntity.ok(new UserWithoutCred(user));
  }

  /**
   * Endpoint to retrieve a user by their ID.
   *
   * @param userId The ID of the user to retrieve.
   * @param token  Authorization token.
   * @return A {@link ResponseEntity} containing the {@link UserWithoutCred} object if the user is found.
   * @throws UnauthoziedExcepetion      If the user is unauthorized or the token is missing.
   * @throws ResponseStatusException    If the user is not found.
   */
  @GetMapping("/users/{userId}")
  public ResponseEntity<UserWithoutCred> getUser(@PathVariable int userId,
      @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (userId != authService.getUserId(token) && !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    User user = userService.getUserById(userId);

    if (user == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    UserWithoutCred userWithoutCred = new UserWithoutCred(user);

    return ResponseEntity.ok(userWithoutCred);
  }

  /**
   * Endpoint to modify the user's password.
   *
   * @param body  A map containing the old password and the new password.
   *              Expected keys: "oldPassword" and "newPassword".
   * @param token Authorization token for the user.
   * @return A response with HTTP status OK if the password was successfully modified.
   * @throws UnauthoziedExcepetion    If the user is unauthorized or the token is missing.
   * @throws IllegalArgumentException If the request body is missing.
   * @throws NotFoundException        If the old or new password is not provided.
   * @throws ConflictException        If the old password is incorrect.
   */
  @PutMapping("/users/modifyPassword")
  public ResponseEntity<HttpStatus> modifyPassword(@RequestBody Map<String, String> body,
      @RequestHeader("Authorization") String token) {

    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (body == null) {
      throw new IllegalArgumentException("Information manquante.");
    }

    if (body.get("newPassword") == null || body.get("oldPassword") == null) {
      String message = "Veuillez fournir un mot de passe.";
      throw new NotFoundException(message);
    }
    String newPassword = body.get("newPassword");
    String oldPassword = body.get("oldPassword");
    if (oldPassword == null) {
      String message = "Veuillez renseignez votre ancien mdp";
      throw new NotFoundException(message);
    }

    Integer userId = authService.getUserId(token);
    if (!userService.checkPassword(userId, oldPassword)) {
      String message = "Mauvais mot de passe ";
      throw new ConflictException(message);
    }
    userService.modifyPassword(userId, newPassword);
    return ResponseEntity.ok(HttpStatus.OK);
  }


  /**
   * Endpoint to validate a company module.
   *
   * @param companyId The ID of the company.
   * @param moduleId  The ID of the module to validate.
   * @param token     Authorization token.
   * @throws UnauthoziedExcepetion    If the user is unauthorized.
   * @throws IllegalArgumentException If the module ID is invalid.
   */
  @PutMapping("/moduleCompany/{companyId}/validate/{moduleId}")
  public void validateModule(@PathVariable int companyId, @PathVariable int moduleId, @RequestHeader("Authorization") String token) {
    if(token == null|| !authService.verifyAdmin(token)){
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }
    if(!authService.verifyAdmin(token)){
        String message = "Vous n'êtes pas autorisé à effectuer cette action.";
        throw new UnauthoziedExcepetion(message);
    }


    if(moduleId == 0){
      String message = "Veuillez fournir un module valide.";
      throw new IllegalArgumentException(message);
    }
    moduleCompanyService.ValidateModuleCompany(companyId, moduleId);
  }

  /**
   * Utils
   */

  /**
   * CommentCompany
   */
  @PatchMapping("/commentCompany/companies/{companyId}")
  public void commentQuestion(@PathVariable int companyId, @RequestBody CommentDTO comment, @RequestHeader("Authorization") String token) {
    if (token == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    Integer userId = authService.getUserId(token);
    if (userId == null) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (userId != companyId && !authService.verifyAdmin(token)) {
      String message = "Vous n'êtes pas autorisé à effectuer cette action.";
      throw new UnauthoziedExcepetion(message);
    }

    if (comment == null || comment.getQuestion() == 0) {
      throw new IllegalArgumentException("Information manquante.");
    }

  CommentCompany commentCompany = commentCompanyService.getCommentByQuestionAndCompany(comment.getQuestion(), companyId);

  if (commentCompany != null) {
    commentCompanyService.updateComment(companyId ,comment.getQuestion(), comment.getComment());
    return;
  }

    commentCompanyService.save(companyId,comment.getQuestion(),  comment.getComment());
  }


  /**
   * Endpoint to retrieve a module company.
   *
   * @param companyId   The ID of the company.
   * @param moduleTitle The title of the module.
   * @return The module company details.
   * @throws IllegalArgumentException If the module title is missing.
   */
  @GetMapping("/moduleCompany/{companyId}/{moduleTitle}")
  public ModuleCompany getModuleCompany(@PathVariable int companyId,@PathVariable String moduleTitle ) {
    if (moduleTitle == null) {
      throw new IllegalArgumentException("Information manquante.");
    }
    return moduleCompanyService.getModuleCompany(companyId, moduleTitle);
  }

}


