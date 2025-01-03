import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root',
})
export class QuestionService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(private router: Router, private userService: UserService) {}

  // Fetch all modules and group questions with answers hierarchically
  async getQuestionsWithAnswers(
    form: string,
    companyId: number
  ): Promise<
    {
      moduleId: number;
      moduleTitle: string;
      children: any[];
      questions: {
        question: { question: string; question_id: number };
        answers: { text: string; answer_id: string }[];
        type: string;
        questionId: string;
        userAnswer?: string | string[];
      }[];
      modulePath: string;
    }[]
  > {
    try {
      const response = await fetch(
        `${this.baseUrl}/moduleAllInfos/${form}/${companyId}`,
        {
          method: 'GET',
          headers: {
            Authorization: `${localStorage.getItem('userToken')}`,
          },
        }
      );

      if (!response.ok) throw new Error('Failed to fetch modules');

      const data = await response.json();

      // Map modules by their IDs
      const moduleMap = new Map<number, any>();

      data.modules.forEach((module: any) => {
        moduleMap.set(module.moduleId, {
          moduleId: module.moduleId,
          moduleTitle: module.title,
          children: [],
          questions: [],
          modulePath: '',
        });
      });

      // Map answers by question ID
      const answerCompanyMap = new Map<number, any[]>();
      data.companyAnswers.forEach((answerCompany: any) => {
        if (!answerCompanyMap.has(answerCompany.question_id)) {
          answerCompanyMap.set(answerCompany.question_id, []);
        }
        answerCompanyMap.get(answerCompany.question_id)?.push(answerCompany);
      });

      if (form === 'ESG') {
        const commentMap = new Map<number, string>();
        data.commentCompanies.forEach((comment: any) => {
          commentMap.set(comment.questionId, comment.comment);
        });

        // Assign questions to modules
        data.questions.forEach((q: any) => {
          const module = moduleMap.get(q.module);
          if (module) {
            // Map related answers for the question
            const relatedAnswers = data.answerESGS
              .filter((answerLink: any) => answerLink.question === q.questionId)
              .map((answerLink: any) => {
                const answer = data.answers.find(
                  (a: any) => a.answerId === answerLink.answerId
                );
                if (answer) {
                  // Check if there's an engagement for this answer
                  const engagementData =
                    (answerCompanyMap.get(q.questionId) || []).find(
                      (sa: any) => sa.answer_id === answer.answerId
                    ) || {};
                  return {
                    text: answer.answer,
                    answer_id: answer.answerId.toString(),
                    isEngagement: !!engagementData.is_engagement, // Add `isEngagement` for the answer
                  };
                }
                return null;
              })
              .filter((answer: any) => answer !== null);

            // Get selected answers from `answerCompanyMap`
            const selectedAnswers = answerCompanyMap.get(q.questionId) || [];

            // Assign userAnswer for OPEN and other question types
            let userAnswer;
            if (q.display === 'MULTIPLE') {
              userAnswer = selectedAnswers.map((sa: any) =>
                sa.answer_id.toString()
              );
            } else if (q.display === 'OPEN') {
              const openAnswer = selectedAnswers[0]?.answer_id;
              if (openAnswer) {
                userAnswer =
                  data.answers.find((a: any) => a.answerId === openAnswer)
                    ?.answer || '';
              } else {
                userAnswer = ''; // No answer, empty string
              }
            } else {
              userAnswer = selectedAnswers.map((sa: any) =>
                sa.answer_id.toString()
              );
            }

            // Push the question into the module
            module.questions.push({
              question: q.title,
              answers: relatedAnswers, // Include the enriched answers with `isEngagement`
              type: q.display,
              questionId: q.questionId.toString(),
              userAnswer, // Add userAnswer as usual
              comment: commentMap.get(q.questionId) || undefined,
            });
          }
        });
      }

      // Similarly for 'ODD'
      if (form === 'ODD') {
        // Assign questions to their modules
        data.questions.forEach((q: any) => {
          const module = moduleMap.get(q.module);
          if (module) {
            const relatedAnswers = data.answerODDS
              .filter((answerLink: any) => answerLink.question === q.questionId)
              .map((answerLink: any) => {
                const answer = data.answers.find(
                  (a: any) => a.answerId === answerLink.answer
                );
                return answer
                  ? {
                      text: answer.answer,
                      answer_id: answer.answerId.toString(),
                    }
                  : null;
              })
              .filter((answer: any) => answer !== null);

            // Get selected answers from the answerCompanyMap
            const selectedAnswers = answerCompanyMap.get(q.questionId) || [];
            const userAnswer = selectedAnswers[0]?.answer_id?.toString(); // Single answer ID for other types
            // Push the question into the module, preserving the questionId

            module.questions.push({
              question: q.title,
              answers: relatedAnswers,
              type: q.display,
              questionId: q.questionId.toString(), // Preserve the original questionId
              userAnswer, // Set as array for MULTIPLE or single value for others
            });
          }
        });
      }
      // Build the hierarchy by assigning child modules to their parents and assigning IDs
      const rootModules: any[] = [];

      const assignModuleId = (modules: any[], parentPath: string = '') => {
        modules.forEach((module, index) => {
          const modulePath =
            parentPath === '' ? `${index + 1}` : `${parentPath}.${index + 1}`;
          module.modulePath = modulePath;

          if (module.children && module.children.length > 0) {
            assignModuleId(module.children, modulePath);
          }
        });
      };

      data.modules.forEach((module: any) => {
        if (module.parentModule) {
          const parentModule = moduleMap.get(module.parentModule);
          if (parentModule) {
            parentModule.children.push(moduleMap.get(module.moduleId));
          }
        } else {
          rootModules.push(moduleMap.get(module.moduleId));
        }
      });

      assignModuleId(rootModules[0].children);
      return rootModules[0].children;
    } catch (error) {
      console.error('Error fetching questions with answers:', error);
      throw error;
    }
  }

  async answerQuestion(
    answerId: string,
    questionId: number,
    token: string,
    companyId: number
  ): Promise<void> {
    try {
      // Check if answerId contains letters (using regex)
      const hasLetters = /[a-zA-Z]/.test(answerId);

      let user = await this.userService.getUser();
      if (!user.isAdmin) {
        companyId = user.userId;
      }


      await fetch(`${this.baseUrl}/answerCompany/companies/${companyId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: token,
        },
        body: JSON.stringify({
          questionId: questionId,
          answerId: answerId,
        }),
      });

      // Create the payload with conditional logic
      const payload = hasLetters
        ? { openAnswer: answerId, questionId } // Complex payload for answerId with letters
        : { answerId, questionId }; // Simple payload for answerId without letters

      // Make the API call
      const response = await fetch(
        `${this.baseUrl}/answerCompany/companies/${companyId}`,
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
          body: JSON.stringify(payload),
        }
      );

      if (!response.ok) {
        const error = await response.json();
        throw new Error(
          `Error: ${error.message || response.statusText} (HTTP ${
            response.status
          })`
        );
      }

    } catch (error) {
      console.error('Error submitting answer:', error);
      throw error;
    }
  }

  async deleteAnswer(
    answerId: number,
    questionId: number | undefined,
    companyId: number | undefined
  ): Promise<void> {
    // Fetch the token from localStorage (or another source)
    const token = localStorage.getItem('userToken');

    if (!token) {
      console.error("Vous n'êtes pas autorisé à effectuer cette action.");
      return;
    }

    // Create the request body
    const requestBody = { answerId, questionId };

    let user = await this.userService.getUser();
    if (!user.isAdmin) {
      companyId = user.userId;
    }

    try {
      const response = await fetch(`${this.baseUrl}/answerCompany/companies/${companyId}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          Authorization: token,
        },
        body: JSON.stringify(requestBody), // Send the object as JSON
      });

      if (!response.ok) {
        // Handle errors
        const errorMessage = await response.text();
        throw new Error(errorMessage || 'Une erreur est survenue.');
      }

    } catch (error) {
      console.error('Erreur lors de la suppression de la réponse:', error);
    }
  }

  async postCommentCompany(
    question: number,
    comment: string,
    companyId: number | undefined
  ): Promise<Response> {
    if (question == -1) {
      throw new Error(`Question id is erronated`);
    }


    let user = await this.userService.getUser();
    if (!user.isAdmin) {
      companyId = user.userId;
    }

    const url = `${this.baseUrl}/commentCompany/companies/${companyId}`; // Construct the full URL
    const payload = { question, comment }; // Payload object
    const token = localStorage.getItem('userToken');
    try {
      const response = await fetch(url, {
        method: 'PATCH',
        headers: {
          'Content-Type': 'application/json', // Inform the server the request body is JSON
          Authorization: `${token}`, // Include the token in the Authorization header
        },
        body: JSON.stringify(payload), // Convert payload to JSON string
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      return response; // Return the response for further handling
    } catch (error) {
      console.error('Error during POST request to /commentCompany:', error);
      throw error; // Propagate the error
    }
  }

  async submitFormESG(companyId: number): Promise<void> {
    try {
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('User token is missing');
      }

      const user = await this.userService.getUser();

      await fetch(`${this.baseUrl}/companies/${companyId}/scoreesg`, {
        method: 'GET',
        headers: {
          Authorization: token,
        },
      });

      if (!user.isAdmin) {
        await fetch(`${this.baseUrl}/companyModule/ESG/dates`, {
          method: 'POST',
          headers: {
            Authorization: token,
          },
        });
      }


      if (!user.isAdmin) {
        // Redirect to /rapport
        this.router.navigate(['/rapport']);
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      throw error;
    }
  }

  async submitFormODD(companyId: number): Promise<void> {
    try {
      const token = localStorage.getItem('userToken');
      if (!token) {
        throw new Error('User token is missing');
      }

      const user = await this.userService.getUser();

      await fetch(`${this.baseUrl}/companies/${companyId}/scoreodd`, {
        method: 'GET',
        headers: {
          Authorization: token,
        },
      });

      if (!user.isAdmin) {
        await fetch(`${this.baseUrl}/companyModule/ODD/dates`, {
          method: 'POST',
          headers: {
            Authorization: token,
          },
        });
      }


      // Redirect to /rapport
      if (!user.isAdmin) {
        this.router.navigate(['/rapport']);
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      throw error;
    }
  }

  async changeCommitmentStatus(
    questionId: number,
    answerId: number,
    status: boolean,
    companyId: number | undefined
  ): Promise<void> {
    try {
      const token = localStorage.getItem('userToken');
      let user = await this.userService.getUser();
      let temp = companyId;

      if (!user.isAdmin) {
        companyId = user.userId;
      }

      if (!token) {
        throw new Error('User token is missing');
      }

      if (!status) {

        await fetch(`${this.baseUrl}/answerCompany/companies/${companyId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
          body: JSON.stringify({
            questionId: questionId,
            answerId: answerId,
          }),
        });

      } else {
        const user = await this.userService.getUser();

        if(!user.isAdmin) {
          companyId = user.userId;
        } else {
          companyId = temp;
        }

        const response = await fetch(
          `${this.baseUrl}/answerCompany/companies/${companyId}`,
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: token,
            },

            body: JSON.stringify({
              questionId: questionId,
              answerId: answerId,
              isEngamement: status,
            }),
          }
        );
      }
    } catch (error) {
      console.error('Error submitting form:', error);
      throw error;
    }
  }
}
