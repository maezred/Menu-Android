1. Does the project appear to meet the technical requirements? Write up one sentence on your findings and give a score 0-3. Yes, the project uses OOP principles, follows Material Design, uses SDK's and API's, and is robust.

Is your peer making API calls, using SDK's/third-party libraries? Yes, looks like you've set up your own API for mexi's and using retrofit for the call.
Is your peer making use of Services? If so, are they offloading long tasks to a separate thread, i.e. AsyncTask, Runnable, IntentService, etc. Using Retrofit to make the API calls which handles threading
Is your peer making use of Fragments? If so, are they passing data from Fragment to Activity via interfaces? If not, why did absense of Fragments make sense? Yes
Is your peer making use of RecyclerView? If so, does it appear to be working correctly ( implementation and otherwise )?
Is your peer making use of some sort of persistent storage, i.e. Firebase or SQLite? If so, why do you think Firebase/SQLite was chosen? Could they have used one or the other instead and why? Yes, recyclerview is used and using a Mexi's database for storage
2. Does the project appear to be creative, innovative, and different from any competition? Write up one sentence on your findings and give a score 0-3. 1 I think your project has been done multiple times, and has a ton of competition. i also think it's a great idea to sell a customized menu app "hand made" from a local small business owner. So keep it up!

Is your peer making use of proper UX patterns we learned in class? If not, what are they doing that is unconvetional or that might confuse a user ( you )? Yes, you are using some advanced techniques
Is your peer making anything cool or awesome that you would like to note or applaud them on? I like how the menu items snap to the top of the page
3. Does the project appear to follow correct coding styles and best practices? Write up one sentence on your findings and give a score 0-3. 3 the code was very easy to understand and read

Are you able to reasonably follow the code without having anyone answer your questions? yes
Are you able to make sense of what the code is doing or is trying to do? Yes
4. Find two pieces of code of any size: one that is readable and easy to follow and one that is difficult to follow and understand. easy to understand: static {
    Retrofit retrofit = new Retrofit.Builder()
      .baseUrl("https://mexxis.mdx.co/data/")
      .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .build();

    service = retrofit.create(Service.class);
  }

  Hard: if (preference instanceof ListPreference) {
        // For list preferences, look up the correct display value in
        // the preference's 'entries' list.
        ListPreference listPreference = (ListPreference) preference;
        int            index          = listPreference.findIndexOfValue(stringValue);

        // Set the summary to reflect the new value.
        preference.setSummary(
          index >= 0
            ? listPreference.getEntries()[index]
            : null);

      }

What makes the readable code readable? Be as detailed as you can in your answer, it can be challenging to explain why something is easy to undertand
What makes the difficult code harder to follow? Be as detailed as you can in your answer. I think all of the code is very well written and easy to understand because the spacing is sync'd and the methods are short and precise. The hard to understand part is more of technical skills thing for me.
5. High level project overview: Take a look at as many individual files as you have time for

Does this class make sense? Yes, the classes are simple and to the point with very few methods that are too hard to understand
Does the structure of the class make sense? Yes the class structures and models make sense to get information
Is it clear what this class is supposed to do? Yes, the classes get menu items back