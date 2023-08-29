/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.hmrcemailrenderer.templates.agent

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{ EitherValues, OptionValues }
import uk.gov.hmrc.hmrcemailrenderer.domain.MessagePriority
import uk.gov.hmrc.hmrcemailrenderer.templates.ServiceIdentifier.Agent
import uk.gov.hmrc.hmrcemailrenderer.templates.{ CommonParamsForSpec, TemplateLoader, TemplateLocator }

class AgentServiceSuspendedAccountSpec
  extends AnyWordSpecLike with Matchers with OptionValues with EitherValues with TemplateLoader
    with CommonParamsForSpec {

  "Agent Service Suspended Account" should {
    val templateLocator = new TemplateLocator {}
    val params = commonParameters ++ Map(
      "agencyName"      -> "Test Agent",
      "arn"             -> "0123456789",
      "contactName"     -> "Joe Blogs",
      "emailAddress"    -> "example@email.com",
      "telephoneNumber" -> "07891234567",
      "agentMessage"    -> "This is a test message!"
    )
    val template = templateLocator
      .templateGroups("Agent")
      .find(_.templateId == "agent_service_suspended_account")
      .get

    "render correct meta information" in {
      template.templateId shouldBe "agent_service_suspended_account"
      template.service shouldBe Agent
      template.fromAddress(Map.empty) shouldBe "HMRC Agent Services <noreply@tax.service.gov.uk>"
      template.subject(Map.empty) shouldBe "HMRC: agent services suspended account"
      template.priority shouldBe Some(MessagePriority.Standard)
    }

    "render correct html content" in {
      val htmlContent = template.htmlTemplate(params).toString

      htmlContent should include("Agent contact about account suspension")
      htmlContent should include("Agent name: Test Agent")
      htmlContent should include("Agent Reference Number: 0123456789")
      htmlContent should include("Contact name: Joe Blogs")
      htmlContent should include("Contact email address: example@email.com")
      htmlContent should include("Contact telephone number: 07891234567")
      htmlContent should include("This agent has given the following details about the suspension of their services account:")
      htmlContent should include("This is a test message!")
      htmlContent should include("From HMRC Agent Services team")
    }

    "render correct text content" in {
      val txtContent = template.plainTemplate(params).toString

      htmlContent should include("Agent contact about account suspension")
      htmlContent should include("Agent name: Test Agent")
      htmlContent should include("Agent Reference Number: 0123456789")
      htmlContent should include("Contact name: Joe Blogs")
      htmlContent should include("Contact email address: example@email.com")
      htmlContent should include("Contact telephone number: 07891234567")
      htmlContent should include("This agent has given the following details about the suspension of their services account:")
      htmlContent should include("This is a test message!")
      htmlContent should include("From HMRC Agent Services team")
    }
  }
}
