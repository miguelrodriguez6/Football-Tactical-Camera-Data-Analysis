package com.tfg.backend.model.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameFrameDao extends JpaRepository<GameFrame, Long> {

    @Query(value = """
                    WITH
                    ataques_banda AS (
                      SELECT
                        game_clock, opta_id,
                        case
                        	when st_distance(ST_SetSRID(ST_MakePoint(:goal, 0),4326),ball_geom)>
                       		 LEAD(st_distance(ball_geom,ST_SetSRID(ST_MakePoint(:goal, 0),4326)),5) OVER (ORDER by game_clock)
                       		 then 1
                       		 else 0
                        end as balon_banda,
                         case
                        	when LEAD(last_touch, 50) OVER (ORDER BY game_clock) = :side
                        	and LEAD(last_touch, 25) OVER (ORDER BY game_clock) = :side
                    	  	and LEAD(last_touch, 100) OVER (ORDER BY game_clock) = :side
                    	  	and LEAD(last_touch, 150) OVER (ORDER BY game_clock) = :side
                    	  	and LAG(last_touch, 25) OVER (ORDER BY game_clock) = :side
                    	  	and LAG(last_touch, 50) OVER (ORDER BY game_clock) = :side
                    	  	then 1
                    	  	else 0
                        end as posesion
                      from
                      game_frame
                      where
                      live=true
                      and team_side= :side
                      and period=1
                      and (st_contains(ST_MakeEnvelope(0, 20, :goal, 40, 4326),ball_geom) or
                      		st_contains(ST_MakeEnvelope(0, -20, :goal, -40, 4326),ball_geom))
                      and last_touch= :side
                      and game_id = :game_id
                    ),
                    jugadas AS (
                      SELECT
                        game_clock,opta_id,
                        CASE
                          WHEN LAG(game_clock) OVER (ORDER BY game_clock) IS NULL
                            OR game_clock - LAG(game_clock) OVER (ORDER BY game_clock) > 10
                          THEN 1
                          ELSE 0
                        END AS inicio_jugada,
                        CASE
                          WHEN LEAD(game_clock) OVER (ORDER BY game_clock) IS NULL
                            OR LEAD(game_clock) OVER (ORDER BY game_clock) - game_clock > 10
                          THEN 1
                          ELSE 0
                        END AS fin_jugada
                      FROM ataques_banda
                      where balon_banda=1
                      and posesion=1
                    ),
                    jugadas_agrupadas AS (
                      SELECT
                        game_clock,opta_id,
                        SUM(inicio_jugada) OVER (ORDER BY game_clock) AS id_jugada
                      FROM jugadas
                        WHERE inicio_jugada = 1 OR fin_jugada = 1
                    )
                    SELECT *
                    FROM (
                      SELECT
                      	opta_id,
                        MIN(game_clock)+57 AS start_time,
                        MAX(game_clock)+67 AS end_time
                      FROM jugadas_agrupadas
                      GROUP BY id_jugada,opta_id
                    ) AS subquery join player pm on subquery.opta_id = pm.opta_id
                    ORDER BY start_time
            """, nativeQuery = true)
    List<Object[]> consultaAtaqueBanda(@Param("side") String side, @Param("goal") int goal, @Param("game_id") int gameId);

    @Query(value = """
                        WITH cambios_de_posesion AS (
                              SELECT
                              	opta_id,
                              	"number",
                                game_clock,
                                last_touch,
                                CASE
                                  WHEN last_touch = :other_side AND LEAD(last_touch,25) OVER (ORDER BY game_clock) = :side
                                  and LEAD(last_touch,75) OVER (ORDER BY game_clock) = :side
                                  and LEAD(last_touch,150) OVER (ORDER BY game_clock) = :side
                                  and st_contains(ST_MakeEnvelope( (:other_goal)*60, -45, 0, 45, 4326),ball_geom)
                                  then 1
                                  ELSE 0
                                END AS cambio_posesion,
                                speed,
                                CASE
                                	WHEN LEAD(speed,25) OVER (ORDER BY game_clock) > speed
                                	THEN 1
                                	ELSE 0
                                END AS velocidad,
                                CASE
                                	WHEN st_distance(ST_SetSRID(ST_MakePoint( (:goal)*60, 0), 4326),lead(ball_geom,150) over (ORDER BY game_clock)) <
                                	st_distance(ST_SetSRID(ST_MakePoint( (:goal)*60, 0), 4326),ball_geom)
                                	THEN 1
                                	ELSE 0
                                END AS balon_contrario,
                                CASE
                                	WHEN st_contains(ST_MakeEnvelope( (:other_goal)*35, -45, (:goal)*60, 45, 4326),lead(ball_geom,250) over (ORDER BY game_clock))
                                	then 1
                                	ELSE 0
                                END AS campo_contrario
                              FROM
                                game_frame
                              WHERE
                                period = 1
                                and live= true
                                and team_side= :side
                                and game_id = :game_id
                            ),
                            contrataques AS (
                              SELECT
                            	opta_id,
                              	"number",
                                game_clock,
                                CASE
                                  WHEN LAG(game_clock) OVER (ORDER BY game_clock) IS NULL
                                       OR game_clock - LAG(game_clock) OVER (ORDER BY game_clock) > 10
                                  THEN 1
                                  ELSE 0
                                END AS inicio_contrataque,
                                CASE
                                  WHEN LEAD(game_clock) OVER (ORDER BY game_clock) IS NULL
                                       OR LEAD(game_clock) OVER (ORDER BY game_clock) - game_clock > 10
                                  THEN 1
                                  ELSE 0
                                END AS fin_contrataque
                              FROM
                                cambios_de_posesion
                              WHERE
                                cambio_posesion = 1
                                AND balon_contrario = 1
                                and velocidad=1
                                and campo_contrario=1
                            ),
                            contrataques_agrupados AS (
                              SELECT
                                opta_id,
                              	"number",
                                game_clock,
                                SUM(inicio_contrataque) OVER (ORDER BY game_clock) AS id_contrataque
                              FROM contrataques
                              WHERE inicio_contrataque = 1 OR fin_contrataque = 1
                            )
                            SELECT *
                            FROM (
                              SELECT
        						opta_id,
                                MIN(game_clock)+57 AS start_time,
                                MAX(game_clock)+67 AS end_time
                              FROM contrataques_agrupados
                              GROUP BY id_contrataque, opta_id, "number"
                            ) AS subquery join player pm on subquery.opta_id = pm.opta_id
                            ORDER BY start_time
        """, nativeQuery = true)

    List<Object[]> consultaContraataque(@Param("side") String side, @Param("other_side") String otherSide, @Param("goal") int goal, @Param("other_goal") int otherGoal, @Param("game_id") int gameId);

    @Query(value = """
                    WITH 
                    jugadores_con_balon AS (
                      SELECT opta_id, game_clock, player_geom, speed, ball_geom,player_large_id,
                      CASE 
                        WHEN LAG(last_touch,5) OVER (ORDER BY game_clock) = :side
                        AND LAG(last_touch,50) OVER (ORDER BY game_clock) = :side
                        AND LAG(last_touch,100) OVER (ORDER BY game_clock) = :side
                        AND LAG(last_touch,150) OVER (ORDER BY game_clock) = :side
                        THEN 1
                        ELSE 0
                      END AS posesion
                      FROM game_frame
                      WHERE team_side = :side
                        AND st_distance(player_geom,ball_geom) <= 1 -- jugadores de tu equipo con el balón a menos de 1 metro
                        AND period = 1
                        AND game_id = :game_id
                    ),
                    jugadores_sin_balon AS (
                      SELECT player_large_id, game_clock, player_geom, speed,opta_id
                      FROM game_frame
                      WHERE team_side = :side  -- jugadores de tu equipo sin el balón
                      AND period = 1
                      AND live=true
                      and ST_Contains(ST_MakeEnvelope(0, -40, :goal, 40, 4326), player_geom)
                     ),
                    jugadores_equipo_contrario AS (
                      SELECT player_large_id, game_clock, player_geom, speed
                      FROM game_frame
                      WHERE team_side = :other_side
                      AND period = 1  
                     ),
                    desmarques AS (
                      SELECT 
                        B.game_clock as gameclock,
                        B.opta_id as optaid,
                        CASE 
                          WHEN B.speed < LEAD(B.speed) OVER (ORDER BY B.game_clock) -- jugador aumenta su velocidad
                         
                          AND st_distance(B.player_geom, C.player_geom) < LEAD(st_distance(B.player_geom, C.player_geom)) OVER (ORDER BY B.game_clock)
                          and LEAD(st_distance(B.player_geom, C.player_geom)) OVER (ORDER BY B.game_clock) <= LEAD(st_distance(B.player_geom, C.player_geom),5) OVER (ORDER BY B.game_clock) -- jugador se aleja de los contrarios
                          
                          and st_distance(ST_SetSRID(ST_MakePoint(:goal, 0),4326),B.player_geom) >
                          LEAD(st_distance(B.player_geom,ST_SetSRID(ST_MakePoint(:goal, 0),4326)),5) OVER (ORDER BY B.game_clock)
                          THEN 1 
                          ELSE 0
                        END AS ruptura
                      FROM 
                        jugadores_con_balon A
                      JOIN 
                        jugadores_sin_balon B ON A.game_clock = B.game_clock AND A.player_large_id != B.player_large_id
                      JOIN 
                        jugadores_equipo_contrario C ON A.game_clock = C.game_clock
                      WHERE 
                        posesion=1
                    ),
                    jugadas AS (
                      SELECT 
                      	optaid,
                        gameclock,
                        CASE 
                          WHEN LAG(gameclock) OVER (ORDER BY gameclock) IS NULL 
                            OR gameclock - LAG(gameclock) OVER (ORDER BY gameclock) > 2
                          THEN 1 
                          ELSE 0 
                        END AS inicio_jugada,
                        CASE 
                          WHEN LEAD(gameclock) OVER (ORDER BY gameclock) IS NULL 
                            OR LEAD(gameclock) OVER (ORDER BY gameclock) - gameclock > 2
                          THEN 1 
                          ELSE 0 
                        END AS fin_jugada
                      FROM desmarques
                      WHERE ruptura = 1
                    ), 
                    jugadas_agrupadas AS (
                      SELECT 
                      	optaid,
                        gameclock,
                        SUM(inicio_jugada) OVER (ORDER BY gameclock) AS id_jugada
                      FROM jugadas
                        WHERE inicio_jugada = 1 OR fin_jugada = 1
                       
                    )
                    SELECT 
                      *
                    FROM (
                      SELECT 
                      	optaid,
                        round(MIN(gameclock)+57) AS start_time,
                        round(MAX(gameclock)+67) AS end_time
                      FROM jugadas_agrupadas
                      GROUP BY id_jugada,optaid
                    ) AS subquery join player pm on subquery.optaid = pm.opta_id
                    ORDER BY start_time
""", nativeQuery = true)
    List<Object[]> consultaDesmarqueEnRuptura(@Param("side") String side, @Param("other_side") String otherSide, @Param("goal") int goal, @Param("game_id") int gameId);

    @Query(value = """
                    WITH
                    jugadores_con_balon AS (
                      SELECT player_large_id, game_clock, player_geom, speed, ball_geom,
                      case
                      	when LAG(last_touch,5) OVER (ORDER BY game_clock)=:side
                      	and LAG(last_touch,50) OVER (ORDER BY game_clock) =:side
                      	and LAG(last_touch,100) OVER (ORDER BY game_clock) =:side
                      	and LAG(last_touch,150) OVER (ORDER BY game_clock) =:side
                      	and st_distance(LAG(player_geom,25) OVER (ORDER BY game_clock),LAG(ball_geom,25) OVER (ORDER BY game_clock))<=1
                      	and st_distance(LAG(player_geom,50) OVER (ORDER BY game_clock),LAG(ball_geom,50) OVER (ORDER BY game_clock))<=1
                      	and st_distance(LAG(player_geom,75) OVER (ORDER BY game_clock),LAG(ball_geom,75) OVER (ORDER BY game_clock))<=1
                      	and st_distance(LAG(player_geom,150) OVER (ORDER BY game_clock),LAG(ball_geom,150) OVER (ORDER BY game_clock))<=1
                    
                      	then 1
                      	else 0
                      end as posesion
                      
                      FROM game_frame
                      WHERE team_side = :side
                      AND st_distance(player_geom,ball_geom) <= 1 -- jugadores de tu equipo con el balón a menos de 1 metro
                      AND period = 1
                      AND game_id = :game_id
                    ),
                    jugadores_sin_balon AS (
                      SELECT player_large_id, game_clock, player_geom, speed ,opta_id
                      FROM game_frame
                      WHERE team_side = :side  -- jugadores de tu equipo sin el balón
                      AND period = 1
                      and live=true
                    
                    ),
                    jugadores_equipo_contrario AS (
                      SELECT player_large_id, game_clock, player_geom, speed
                      FROM game_frame
                      WHERE team_side = :other_side
                      AND period = 1
                    ),
                    desmarques as (SELECT
                      A.game_clock as gameclock,
                      B.opta_id as optaid,
                      case
                      	when st_distance(B.player_geom,C.player_geom)<
                      	st_distance(LEAD(B.player_geom,5) OVER (ORDER BY A.game_clock),LEAD(C.player_geom,5) OVER (ORDER BY A.game_clock))
                      	and
                      	st_distance(B.player_geom,A.ball_geom)>
                      	st_distance(LEAD(B.player_geom,5) OVER (ORDER BY A.game_clock),LEAD(A.ball_geom,5) OVER (ORDER BY A.game_clock))
                     	and
                     	st_distance(B.player_geom,A.player_geom)>
                      	st_distance(LEAD(B.player_geom) OVER (ORDER BY A.game_clock),LEAD(A.player_geom) OVER (ORDER BY A.game_clock))
                      	then 1
                      	else 0
                      end as jugador_alejandose,
                      case
                      	when B.speed< LEAD(B.speed,5) OVER (ORDER BY A.game_clock)
                      	then 1
                      	else 0
                      end as acelera
                    FROM
                      jugadores_con_balon A
                    JOIN
                      jugadores_sin_balon B ON A.game_clock = B.game_clock AND A.player_large_id != B.player_large_id
                    JOIN
                      jugadores_equipo_contrario C ON A.game_clock = C.game_clock
                    WHERE
                      ST_Distance(B.player_geom, C.player_geom) <= 3
                      and posesion=1
                    ),
                    jugadas AS (
                      SELECT
                        optaid,
                        gameclock,
                        CASE
                          WHEN LAG(gameclock) OVER (ORDER BY gameclock) IS NULL
                               OR gameclock - LAG(gameclock) OVER (ORDER BY gameclock) > 5 --10
                          THEN 1 
                          ELSE 0 
                        END AS inicio_jugada,
                        CASE 
                          WHEN LEAD(gameclock) OVER (ORDER BY gameclock) IS NULL 
                               OR LEAD(gameclock) OVER (ORDER BY gameclock) - gameclock > 5 --10
                          THEN 1 
                          ELSE 0 
                        END AS fin_jugada
                       from desmarques
                       where jugador_alejandose=1
                       and acelera=1
                    ), 
                    jugadas_agrupadas AS (
                      SELECT 
                        optaid,
                        gameclock,
                        SUM(inicio_jugada) OVER (ORDER BY gameclock) AS id_jugada
                      FROM jugadas
                      WHERE inicio_jugada = 1 OR fin_jugada = 1
                    )
                    select *
                    from(
                    SELECT 
                      optaid,
                      MIN(gameclock)+57 AS start_time,
                      MAX(gameclock)+67 AS end_time
                    FROM jugadas_agrupadas
                    GROUP BY id_jugada,optaid) as subquery join player pm on subquery.optaid = pm.opta_id
                    order by start_time
  """, nativeQuery=true)
    List<Object[]> consultaDesmarqueEnApoyo(@Param("side") String side, @Param("other_side") String otherSide, @Param("game_id") int gameId);

    @Query(value = "SELECT COUNT(DISTINCT g.frame_id) FROM game_frame g WHERE g.game_id = :gameId", nativeQuery = true)
    int countDistinctFrameIdsByGameId(@Param("gameId") Integer gameId);

    @Query(value = "SELECT COUNT(DISTINCT g.frame_id) FROM game_frame g WHERE g.game_id = :gameId AND g.last_touch='home'", nativeQuery = true)
    int countDistinctFrameIdsByGameIdAndHomeLastTouch(@Param("gameId") Integer gameId);
}
