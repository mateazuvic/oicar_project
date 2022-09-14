using Bunit;
using Microsoft.JSInterop;
using Moq;
using NUnit.Framework;
using Radzen.Blazor;
using Radzen.Blazor.Rendering;
using RichardSzalay.MockHttp;

namespace eSouvenirTest
{
    public class Tests
    {
        [SetUp]
        public void Setup()
        {
            
        }

        [Test]
        public void WhenInfoButtonClicked_UserGraphicsShown()
        {
            //Arrange
           
            using var context = new Bunit.TestContext();
            context.JSInterop.Setup<Rect>("Radzen.createChart", _ => true);
            context.Services.AddMockHttpClient();
            context.JSInterop.SetupVoid("Radzen.adjustDataGridHeader", _ => true);

            //Act
            var cut = context.RenderComponent<eSouvenirWebApp.Pages.UserStatistics>();
            var element = cut.Find("RadzenButton");
            cut.Find("RadzenButton").Click();

            //Assert
            context.JSInterop.ShouldBeElementReferenceTo(element);

            //Assert.Pass();
        }
    }
}