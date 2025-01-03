import { Injectable } from '@angular/core';
import { PDFDocument, StandardFonts, rgb, PDFPage, PDFFont } from 'pdf-lib';

interface Module {
  moduleId: number;
  companyId: number;
  date_form: string;
  score: number;
  score_engagement: number;
  scoremax: number;
}

interface SubModule {
  module: Module;
  title: string;
  subModules: SubModule[];
}

interface ReportResponse {
  module: Module;
  title: string;
  subModules: SubModule[];
}

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private baseUrl = 'http://localhost:8080/api';

  constructor() {}

  async getReport(form: string): Promise<ReportResponse> {
    try {
      const responseUser = await fetch(`${this.baseUrl}/users/token`, {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('userToken')}`,
        },
      });
      if (!responseUser.ok) {
        throw new Error('Failed to fetch User');
      }
      const user = await responseUser.json();

      const responseModule = await fetch(
        `${this.baseUrl}/moduleCompanyWithSubmodules/${form}/company/${user.userId}`,
        {
          method: 'GET',
          headers: {
            Authorization: `${localStorage.getItem('userToken')}`,
          },
        }
      );
      if (!responseModule.ok) {
        throw new Error('Failed to fetch ReportService');
      }
      const data = await responseModule.json();
      return data;
    } catch (error) {
      console.error('Error fetching ReportService:', error);
      throw error;
    }
  }

  async getCommitments(): Promise<
    Record<string, Array<Record<string, string | null>>>
  > {
    try {
      const responseUser = await fetch(`${this.baseUrl}/users/token`, {
        method: 'GET',
        headers: {
          Authorization: `${localStorage.getItem('userToken')}`,
        },
      });
      if (!responseUser.ok) {
        throw new Error('Failed to fetch User');
      }
      const user = await responseUser.json();

      const commitments = await fetch(
        `${this.baseUrl}/companies/${user.userId}/answer/engagement`,
        {
          method: 'GET',
          headers: {
            Authorization: `${localStorage.getItem('userToken')}`,
          },
        }
      );
      if (!commitments.ok) {
        throw new Error('Failed to fetch commitments');
      }
      return commitments.json();
    } catch (error) {
      console.error('Error fetching glossaire:', error);
      throw error;
    }
  }

  async generatePdfFromJson(reportODD: any, reportESG: any): Promise<void> {
    const pdfDoc = await PDFDocument.create();
    const timesRomanFont = await pdfDoc.embedFont(StandardFonts.TimesRoman);

    let page = pdfDoc.addPage();
    const { width, height } = page.getSize();

    let y = page.getY();
    y = height - 50; // Start from the top of the page

    const titleFontSize = 36;
    const titleScoreFontSize = 24;
    const fontSize = 14;
    const spaceLine = fontSize + 5;

    let pageNumber = 1;

    // Add main title for the ODD report
    page.drawText(reportODD.title, {
      x: width / 2 - 40,
      y: y,
      size: titleFontSize,
      font: timesRomanFont,
      color: rgb(0.18, 0.36, 0.18),
    });

    page.drawLine({
      start: { x: width / 2 - 40, y: y - 3 },
      end: { x: width / 2 + 40, y: y - 3 },
      thickness: 1,
      color: rgb(0.18, 0.36, 0.18),
      opacity: 1,
    });

    page.drawText(`Page ${pageNumber}`, {
      x: page.getWidth() - 90,
      y: 50,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0, 0),
    });

    y -= spaceLine * 4;

    let text = 'Score ' + reportODD.title + ' : ' + reportODD.module.score + ' %';
    page.drawText(text, {
      x: 50,
      y: y,
      size: titleScoreFontSize,
      font: timesRomanFont,
      color: rgb(0.18, 0.36, 0.18),
    });

    y -= spaceLine * 4;

    // Traverse through the submodules and add them to the PDF
    for (const subModule of reportODD.subModules) {
      ({ y, page, pageNumber } = this.addSubModuleToPdf(
        pdfDoc,
        page,
        subModule,
        y,
        timesRomanFont,
        fontSize,
        pageNumber
      ));

      if (y - spaceLine * 5 > spaceLine * 6) {
        y -= spaceLine * 2;

        if (
          subModule !== reportODD.subModules[reportODD.subModules.length - 1]
        ) {
          page.drawLine({
            start: { x: 40, y },
            end: { x: page.getWidth() - 40, y },
            thickness: 1,
            color: rgb(0.18, 0.36, 0.18),
            opacity: 1,
          });
        }

        y -= spaceLine * 3;
      } else {
        y -= spaceLine * 5;
      }

      // Check if a new page is needed
      if (y < spaceLine * 6) {
        page = pdfDoc.addPage();

        pageNumber += 1;

        page.drawText(`Page ${pageNumber}`, {
          x: page.getWidth() - 100,
          y: 50,
          size: fontSize,
          font: timesRomanFont,
          color: rgb(0, 0, 0),
        });

        y = height - 100; // Start from the top of the page
      }
    }

    page = pdfDoc.addPage();

    pageNumber += 1;

    page.drawText(`Page ${pageNumber}`, {
      x: page.getWidth() - 100,
      y: 50,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0, 0),
    });

    y = height - 50; // Start from the top of the page

    // Add main title for the ESG report
    page.drawText(reportESG.title, {
      x: width / 2 - 35,
      y: y,
      size: titleFontSize,
      font: timesRomanFont,
      color: rgb(0.18, 0.36, 0.18),
    });

    page.drawLine({
      start: { x: width / 2 - 35, y: y - 3 },
      end: { x: width / 2 + 35, y: y - 3 },
      thickness: 1,
      color: rgb(0.18, 0.36, 0.18),
      opacity: 1,
    });

    y -= spaceLine * 4;

    text = 'Score ' + reportESG.title + ' : ' + reportESG.module.score + ' / 90';
    page.drawText(text, {
      x: 50,
      y: y,
      size: titleScoreFontSize,
      font: timesRomanFont,
      color: rgb(0.18, 0.36, 0.18),
    });

    y -= spaceLine * 4;

    // Traverse through the submodules and add them to the PDF
    for (const subModule of reportESG.subModules) {
      ({ y, page, pageNumber } = this.addSubModuleToPdf(
        pdfDoc,
        page,
        subModule,
        y,
        timesRomanFont,
        fontSize,
        pageNumber
      ));

      if (y - spaceLine * 5 > spaceLine * 6) {
        y -= spaceLine * 2;

        if (
          subModule !== reportESG.subModules[reportESG.subModules.length - 1]
        ) {
          page.drawLine({
            start: { x: 40, y },
            end: { x: page.getWidth() - 40, y },
            thickness: 1,
            color: rgb(0.18, 0.36, 0.18),
            opacity: 1,
          });
        }

        y -= spaceLine * 3;
      } else {
        y -= spaceLine * 5;
      }

      // Check if a new page is needed
      if (y < spaceLine * 6) {
        page = pdfDoc.addPage();

        pageNumber += 1;

        page.drawText(`Page ${pageNumber}`, {
          x: page.getWidth() - 100,
          y: 50,
          size: fontSize,
          font: timesRomanFont,
          color: rgb(0, 0, 0),
        });

        y = height - 100; // Start from the top of the page
      }
    }

    // Finalize the PDF creation
    const pdfBytes = await pdfDoc.save();

    // Optionally: Create a Blob or download the PDF
    const blob = new Blob([pdfBytes], { type: 'application/pdf' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = 'combined-report.pdf';
    link.click();
  }

  // Helper function to recursively add submodules to the PDF
  private addSubModuleToPdf(
    pdfDoc: PDFDocument,
    page: PDFPage,
    subModule: any,
    y: number,
    font: PDFFont,
    fontSize: number,
    pageNumber: number
  ): { y: number; page: PDFPage; pageNumber: number } {
    // Add submodule title
    const { width, height } = page.getSize();
    const spaceLine = fontSize + 5;

    page.drawText(subModule.title + '.', {
      x: 50,
      maxWidth: 500,
      y: y,
      size: fontSize,
      font,
      color: rgb(0, 0, 0),
    });

    const mult = Math.ceil(subModule.title.length / 45);

    y -= spaceLine * mult;

    // Add submodule details
    if (subModule.module) {
      let text = '';

      if (subModule.module.scoremax === 0) {
        text = `Score: ${subModule.module.score} %`;
      } else {
        text = `Score: ${subModule.module.score} / ${subModule.module.scoremax}`;
      }

      page.drawText(text, {
        x: 70,
        y: y,
        size: fontSize,
        font,
        color: rgb(0, 0, 0),
      });
    }

    return { y, page, pageNumber };
  }
}
