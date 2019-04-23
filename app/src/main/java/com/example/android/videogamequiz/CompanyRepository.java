package com.example.android.videogamequiz;

import java.util.ArrayList;
import java.util.List;

public class CompanyRepository {
    public static List<Company> getCompanies() {
        List<Company> result = new ArrayList<>();

        ArrayList<Game> games = new ArrayList<>();
        games.add(new Game("Red Dead Redemtion 2", R.drawable.main_question_game_red_dead_redemption));
        games.add(new Game("Grand Theft Auto 5",  R.drawable.main_question_game_gta_5));
        games.add(new Game("L. A. Noire",  R.drawable.main_question_game_la_noire));
        games.add(new Game("Bully",  R.drawable.main_question_game_bully));
        result.add(new Company(games,  R.drawable.main_question_company_rockstar, "Rockstar Games"));

        games = new ArrayList<>();
        games.add(new Game("Spiro",  R.drawable.main_question_game_spider_man));
        games.add(new Game("Call of Duty WWII",  R.drawable.main_question_game_cod_ww2));
        games.add(new Game("Tony Hawk's Pro Skater 5",  R.drawable.main_question_game_tony_hawks_pro_skater_5));
        games.add(new Game("The Amazing Spider-Man 2",  R.drawable.main_question_game_spider_man));
        result.add(new Company(games,  R.drawable.main_question_company_activision, "Activision"));

        games = new ArrayList<>();
        games.add(new Game("Overwatch",  R.drawable.main_question_game_overwatch));
        games.add(new Game("World of Warcraft",  R.drawable.main_question_game_wow));
        games.add(new Game("Diablo Immortal",  R.drawable.main_question_game_diablo_immortal));
        games.add(new Game("Starcraft 2",  R.drawable.main_question_game_starcraft_2));
        result.add(new Company(games,  R.drawable.main_question_company_blizzard, "Blizzard"));

        games = new ArrayList<>();
        games.add(new Game("Tom Clancy's the Division 2",  R.drawable.main_question_game_division));
        games.add(new Game("Far Cry 5",  R.drawable.main_question_game_far_cry_5));
        games.add(new Game("For Honour",  R.drawable.main_question_game_for_honour));
        games.add(new Game("Just Dance 2019",  R.drawable.main_question_game_just_dance_2019));
        result.add(new Company(games,  R.drawable.main_question_company_ubisoft, "Ubisoft"));

        games = new ArrayList<>();
        games.add(new Game("Pro Evolution Soccer 2019",  R.drawable.main_question_game_pes_2019));
        games.add(new Game("Metal Gear Solid 5",  R.drawable.main_question_game_mgs_5));
        games.add(new Game("Suikoden 2",  R.drawable.main_question_game_suikoden_2));
        games.add(new Game("Castlevania 2",  R.drawable.main_question_game_castlevania_2));
        result.add(new Company(games,  R.drawable.main_question_company_konami, "Konami"));

        games = new ArrayList<>();
        games.add(new Game("Apex Legends",  R.drawable.main_question_game_apex_legends));
        games.add(new Game("Anthem",  R.drawable.main_question_game_anthem));
        games.add(new Game("Sims 4",  R.drawable.main_question_game_sims_4));
        games.add(new Game("Fifa 19",  R.drawable.main_question_game_fifa_19));
        result.add(new Company(games,  R.drawable.main_question_company_ea, "Electronic Arts"));

        return result;
    }
}
