using eSouvenirWebApp.Interfaces;
using eSouvenirWebApp.Services;
using IgniteUI.Blazor.Controls;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Radzen;
using Syncfusion.Blazor;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

namespace eSouvenirWebApp
{
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebAssemblyHostBuilder.CreateDefault(args);
            builder.RootComponents.Add<App>("#app");
            builder.Services.AddScoped<IUserService, UserService>();
            
            builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri("https://oicartim04app.azurewebsites.net") });
            //builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri("https://localhost:44377/") });
            //builder.Services.AddScoped(typeof(IIgniteUIBlazor), typeof(IgniteUIBlazor));
            //builder.Services.AddScoped(typeof(ISyncfusionStringLocalizer), typeof(SyncfusionStringLocalizer));
            //builder.Services.AddSingleton<IgniteUIBlazor>();

            builder.Services.AddScoped<DialogService>();
            builder.Services.AddScoped<NotificationService>();
            builder.Services.AddScoped<TooltipService>();
            builder.Services.AddScoped<ContextMenuService>();

            builder.Services.AddSingleton<AppState>();

            //AddTrasient
            //komentar
            //Jolin komentar
            //Jolin komentar 2


            //lukin treci pokusaj
            await builder.Build().RunAsync();
        }
    }
}
